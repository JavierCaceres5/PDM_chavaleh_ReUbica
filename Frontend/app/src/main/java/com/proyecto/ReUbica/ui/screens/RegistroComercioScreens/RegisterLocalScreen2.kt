package com.proyecto.ReUbica.ui.screens.RegistroComercioScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.compose.*
import com.proyecto.ReUbica.BuildConfig
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.layouts.StepTopBar
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen3Navigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.AnnotatedString



@Composable
fun RegisterLocalScreen2(navController: NavHostController, viewModel: RegistroComercioViewModel) {
    RegisterLocalScreen2Content(
        registroComercio = viewModel,
        onNext = { navController.navigate(RegisterLocalScreen3Navigation) },
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun RegisterLocalScreen2Content(
    registroComercio: RegistroComercioViewModel,
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val abel = FontFamily(Font(R.font.abelregular))
    val poppins = FontFamily(Font(R.font.poppinsextrabold))

    val coroutineScope = rememberCoroutineScope()

    val cameraPositionState = rememberCameraPositionState()
    val markerState = remember { MarkerState(position = LatLng(13.6929, -89.2182)) }
    var mapLoaded by remember { mutableStateOf(false) }

    val emprendimiento by registroComercio.emprendimiento.collectAsState()
    val redesSociales = emprendimiento.redes_sociales

    val camposValidos = emprendimiento.direccion.isNotBlank() &&
            emprendimiento.latitud != 0.0 && emprendimiento.longitud != 0.0

    val context = LocalContext.current
    val placesClient = remember {
        Places.createClient(context)
    }
    val suggestions = remember { mutableStateListOf<AutocompletePrediction>() }
    var showDropdown by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(false) }
    var showRedes by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }





    var errorMessage by remember { mutableStateOf<String?>(null) }
    val error by registroComercio.error.collectAsState()

    val showError = errorMessage ?: error

    LaunchedEffect(markerState.position, mapLoaded) {
        if (mapLoaded) {
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(markerState.position, 16f))
        }
    }

    Column(Modifier.fillMaxSize()) {
        StepTopBar(step = 2, title = "Datos del negocio", onBackClick = onBack)

        val scrollState = rememberScrollState()

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LaunchedEffect(emprendimiento.direccion, mapLoaded) {
                if (emprendimiento.direccion.isNotBlank() && mapLoaded) {
                    coroutineScope.launch {
                        val coords = getCoordinatesFromAddress(emprendimiento.direccion)
                        coords?.let {
                            markerState.position = it
                            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 16f))
                            registroComercio.setValues("latitud", it.latitude.toString())
                            registroComercio.setValues("longitud", it.longitude.toString())
                        }
                    }
                }
            }

            Text(
                text = "¡Continuemos con tu registro!",
                fontFamily = poppins,
                fontSize = 24.sp,
                color = Color(0xFF5A3C1D),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Esta información será visible en tu perfil y permitirá a los clientes conocer lo que ofreces en tu negocio.",
                fontFamily = abel,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            if (showError != null) {
                Text(
                    text = showError,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp).padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Teléfono",
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = emprendimiento.emprendimientoPhone,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }.take(8)

                    val formatted = when {
                        digits.length > 4 -> digits.substring(0, 4) + "-" + digits.substring(4)
                        else -> digits
                    }

                    registroComercio.setValues("telefono", formatted)
                }

                ,
                placeholder = {
                    Text("Teléfono del local", fontFamily = abel, color = Color(0xFF5A3C1D))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFDFF2E1),
                    unfocusedContainerColor = Color(0xFFDFF2E1)
                ),
                shape = RoundedCornerShape(8.dp),
                textStyle = LocalTextStyle.current.copy(color = Color.Black, fontFamily = abel)
            )


            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Dirección completa del local",
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.fillMaxWidth()
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = emprendimiento.direccion,
                    onValueChange = {
                        registroComercio.setValues("direccion", it)
                        showDropdown = true

                        coroutineScope.launch {
                            val request = FindAutocompletePredictionsRequest.builder()
                                .setQuery(it)
                                .setCountries("SV")
                                .setTypeFilter(TypeFilter.ADDRESS)
                                .build()

                            placesClient.findAutocompletePredictions(request)
                                .addOnSuccessListener { response ->
                                    suggestions.clear()
                                    suggestions.addAll(response.autocompletePredictions)
                                }
                                .addOnFailureListener { }
                        }
                    },
                    placeholder = { Text("Buscar", fontFamily = abel, color = Color.Black) },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    leadingIcon = {
                        IconButton(onClick = {
                            if (emprendimiento.direccion.isNotBlank()) {
                                coroutineScope.launch {
                                    val coords = getCoordinatesFromAddress(emprendimiento.direccion)
                                    coords?.let {
                                        markerState.position = it
                                        if (mapLoaded) {
                                            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 16f))
                                        }
                                        registroComercio.setValues("latitud", it.latitude.toString())
                                        registroComercio.setValues("longitud", it.longitude.toString())
                                    }
                                }
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Black)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFDFF2E1), shape = RoundedCornerShape(8.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )

                if (showDropdown && suggestions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFF49724C), shape = RoundedCornerShape(10.dp))
                            .padding(4.dp)
                            .heightIn(max = 200.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        suggestions.forEachIndexed { index, prediction ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFDFF2E1))
                                    .clickable {
                                        val selectedAddress = prediction.getFullText(null).toString()
                                        registroComercio.setValues("direccion", selectedAddress)
                                        showDropdown = false
                                        suggestions.clear()

                                        coroutineScope.launch {
                                            val coords = getCoordinatesFromAddress(selectedAddress)
                                            coords?.let {
                                                markerState.position = it
                                                if (mapLoaded) {
                                                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 16f))
                                                }
                                                registroComercio.setValues("latitud", it.latitude.toString())
                                                registroComercio.setValues("longitud", it.longitude.toString())
                                            }
                                        }

                                    }
                                    .padding(vertical = 10.dp, horizontal = 14.dp)
                            ) {
                                Text(
                                    text = prediction.getFullText(null).toString(),
                                    color = Color.Black,
                                    fontFamily = abel,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }
                            if (index != suggestions.lastIndex) {
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }
            }

            TextButton(
                onClick = { showMap = !showMap },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(if (showMap) "Ocultar mapa" else "Ver mapa", color = Color(0xFF013F6D))
            }

            if (showMap) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                ) {
                    GoogleMap(
                        modifier = Modifier.matchParentSize(),
                        cameraPositionState = cameraPositionState,
                        onMapLoaded = { mapLoaded = true }
                    ) {
                        Marker(state = markerState)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Redes sociales", fontFamily = poppins)
                TextButton(onClick = { showRedes = !showRedes }) {
                    Text(if (showRedes) "Ocultar" else "Ver más", color = Color(0xFF013F6D))
                }
            }

            if (showRedes) {
                RedesSociales(R.drawable.instagram, redesSociales.Instagram ?: "") {
                    registroComercio.setRedesSociales("Instagram", it)
                }
                RedesSociales(R.drawable.facebook, redesSociales.Facebook ?: "") {
                    registroComercio.setRedesSociales("Facebook", it)
                }
                RedesSociales(R.drawable.tiktok, redesSociales.TikTok ?: "") {
                    registroComercio.setRedesSociales("TikTok", it)
                }
                RedesSociales(R.drawable.x, redesSociales.Twitter ?: "") {
                    registroComercio.setRedesSociales("Twitter", it)
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            if (showValidationError) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(Color(0xFFFFE6E6), shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFD32F2F), shape = RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Por favor, complete todos los campos antes de continuar.",
                        color = Color(0xFFD32F2F),
                        fontSize = 14.sp,
                        fontFamily = abel,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            Button(
                onClick = {
                    val telefonoValido = Regex("^\\d{4}-\\d{4}$").matches(emprendimiento.emprendimientoPhone)
                    val direccionValida = emprendimiento.direccion.isNotBlank()
                    val coordenadasValidas = emprendimiento.latitud != 0.0 && emprendimiento.longitud != 0.0


                    if (!telefonoValido || !direccionValida || !coordenadasValidas) {
                        showValidationError = true
                    } else {
                        showValidationError = false
                        onNext()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF49724C),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Continuar a la carta de productos", fontFamily = abel)
            }



            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RedesSociales(iconId: Int, value: String, onValueChange: (String) -> Unit) {
    val abel = FontFamily(Font(R.font.abelregular))

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp))
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFDFF2E1), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = "Ingrese su usuario",
                        fontFamily = abel,
                        color = Color.Black
                    )
                },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

suspend fun getCoordinatesFromAddress(address: String): LatLng? = withContext(Dispatchers.IO) {
    try {
        val apiKey = BuildConfig.MAPS_API_KEY
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=${address.replace(" ", "+")}&components=country:SV&key=$apiKey"
        val response = URL(url).readText()
        val json = JSONObject(response)

        if (json.getString("status") == "OK") {
            val location = json.getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location")
            LatLng(location.getDouble("lat"), location.getDouble("lng"))
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}
