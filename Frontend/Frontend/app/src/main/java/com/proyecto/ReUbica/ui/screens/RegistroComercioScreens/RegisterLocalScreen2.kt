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
import com.proyecto.ReUbica.ui.Components.RedesSociales

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
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var mapLoaded by remember { mutableStateOf(false) }
    var isByCoordinates by remember { mutableStateOf(false) }

    val emprendimiento by registroComercio.emprendimiento.collectAsState()
    val redesSociales = emprendimiento.redes_sociales

    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }
    val suggestions = remember { mutableStateListOf<AutocompletePrediction>() }
    var showDropdown by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(false) }
    var showRedes by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }
    var errorUrl by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val error by registroComercio.error.collectAsState()
    val showError = errorMessage ?: error

    // --- Mantén el mapa centrado al marker actual
    LaunchedEffect(markerPosition, mapLoaded) {
        markerPosition?.let {
            if (mapLoaded) {
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 16f))
            }
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
            // --- Dirección cambiada por texto (no coordenada manual) actualiza markerPosition
            LaunchedEffect(emprendimiento.direccion, mapLoaded) {
                if (emprendimiento.direccion.isNotBlank() && mapLoaded && !isByCoordinates) {
                    coroutineScope.launch {
                        val coords = getCoordinatesFromAddress(emprendimiento.direccion)
                        coords?.let {
                            markerPosition = it
                            registroComercio.setValues("latitud", it.latitude.toString())
                            registroComercio.setValues("longitud", it.longitude.toString())
                        }
                    }
                }
                isByCoordinates = false
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
                },
                placeholder = { Text("0000-0000", fontFamily = abel, color = Color(0xFF5A3C1D)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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
                    onValueChange = { input ->
                        registroComercio.setValues("direccion", input)
                        showDropdown = true
                        // Aquí SOLO actualiza texto y dropdown
                        if (input.isBlank()) {
                            registroComercio.setValues("latitud", "")
                            registroComercio.setValues("longitud", "")
                            suggestions.clear()
                            markerPosition = null
                        } else {
                            // Solo buscar sugerencias (NUNCA buscar coordenadas aquí)
                            coroutineScope.launch {
                                val request = FindAutocompletePredictionsRequest.builder()
                                    .setQuery(input)
                                    .setCountries("SV")
                                    .setTypeFilter(TypeFilter.ADDRESS)
                                    .build()
                                placesClient.findAutocompletePredictions(request)
                                    .addOnSuccessListener { response ->
                                        suggestions.clear()
                                        suggestions.addAll(response.autocompletePredictions)
                                    }
                            }
                        }
                    },
                    placeholder = { Text("Buscar", fontFamily = abel, color = Color.Black) },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    leadingIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                val regex = Regex("""^\s*(-?\d+(\.\d+)?)[,; ]\s*(-?\d+(\.\d+)?)\s*$""")
                                val match = regex.matchEntire(emprendimiento.direccion)
                                if (match != null) {
                                    // Es coordenada
                                    val lat = match.groupValues[1].toDoubleOrNull()
                                    val lng = match.groupValues[3].toDoubleOrNull()
                                    if (lat != null && lng != null) {
                                        isByCoordinates = true
                                        val address = getAddressFromCoordinates(lat, lng, context)
                                        if (address != null) {
                                            registroComercio.setValues("direccion", address)
                                        }
                                        registroComercio.setValues("latitud", lat.toString())
                                        registroComercio.setValues("longitud", lng.toString())
                                        markerPosition = LatLng(lat, lng)
                                        showDropdown = false
                                        return@launch
                                    }
                                }
                                // Es dirección
                                val coords = getCoordinatesFromAddress(emprendimiento.direccion)
                                coords?.let {
                                    markerPosition = it
                                    registroComercio.setValues("latitud", it.latitude.toString())
                                    registroComercio.setValues("longitud", it.longitude.toString())
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
                                                markerPosition = it
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
                        markerPosition?.let { pos ->
                            Marker(state = MarkerState(position = pos))
                        }
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
                        text = "Por favor, complete todos los campos antes de continuar",
                        color = Color(0xFFD32F2F),
                        fontSize = 14.sp,
                        fontFamily = abel,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (errorUrl) {
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
                        text = "Las redes sociales deben ser URLs válidas.",
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

                    val urlRegex = Regex("^(https?://)?(www\\.)?([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}(/.*)?$")

                    val instagramValido = redesSociales.Instagram.isNullOrBlank() || urlRegex.matches(redesSociales.Instagram!!)
                    val facebookValido = redesSociales.Facebook.isNullOrBlank() || urlRegex.matches(redesSociales.Facebook!!)
                    val tiktokValido = redesSociales.TikTok.isNullOrBlank() || urlRegex.matches(redesSociales.TikTok!!)
                    val twitterValido = redesSociales.Twitter.isNullOrBlank() || urlRegex.matches(redesSociales.Twitter!!)

                    when {
                        !telefonoValido || !direccionValida || !coordenadasValidas -> {
                            showValidationError = true
                            errorUrl = false
                        }
                        !instagramValido || !facebookValido || !tiktokValido || !twitterValido -> {
                            showValidationError = false
                            errorUrl = true
                        }
                        else -> {
                            showValidationError = false
                            errorUrl = false
                            errorMessage = null
                            onNext()
                        }
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

// --- Tus funciones de geolocalización igual que antes ---

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

suspend fun getAddressFromCoordinates(
    lat: Double,
    lng: Double,
    context: android.content.Context
): String? = withContext(Dispatchers.IO) {
    try {
        val apiKey = com.proyecto.ReUbica.BuildConfig.MAPS_API_KEY
        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat,$lng&key=$apiKey"
        val response = URL(url).readText()
        val json = JSONObject(response)
        if (json.getString("status") == "OK") {
            json.getJSONArray("results")
                .getJSONObject(0)
                .getString("formatted_address")
        } else null
    } catch (e: Exception) {
        null
    }
}
