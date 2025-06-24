package com.proyecto.ReUbica.ui.screens.RegistroComercioScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.layouts.StepTopBar
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen3Navigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import androidx.navigation.NavHostController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import com.proyecto.ReUbica.BuildConfig

@Composable
fun RegisterLocalScreen2(navController: NavHostController) {
    RegisterLocalScreen2Content(
        onNext = { navController.navigate(RegisterLocalScreen3Navigation) },
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun RegisterLocalScreen2Content(
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val abel = FontFamily(Font(R.font.abelregular))
    val poppins = FontFamily(Font(R.font.poppinsextrabold))

    var horario by remember { mutableStateOf("") }
    var direccionInput by remember { mutableStateOf(TextFieldValue("")) }
    var direccionConfirmada by remember { mutableStateOf("") }


    var showMap by remember { mutableStateOf(false) }
    var showRedes by remember { mutableStateOf(false) }

    var instagram by remember { mutableStateOf("") }
    var facebook by remember { mutableStateOf("") }
    var tiktok by remember { mutableStateOf("") }
    var x by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var location by remember { mutableStateOf(LatLng(13.6929, -89.2182)) }

    val cameraPositionState = rememberCameraPositionState()
    val markerState = remember { MarkerState(position = location) }

    val camposValidos = horario.isNotBlank() && direccionInput.text.isNotBlank()

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

            Spacer(modifier = Modifier.height(16.dp))

            Text("Horario de atención", fontFamily = poppins, fontSize = 14.sp, color = Color(0xFF5A3C1D), modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = horario,
                onValueChange = { horario = it },
                placeholder = { Text("Días y hora", fontFamily = abel, color = Color.Black) },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFDFF2E1), shape = RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Dirección completa del local", fontFamily = poppins, fontSize = 14.sp, color = Color(0xFF5A3C1D), modifier = Modifier.fillMaxWidth())

            val context = LocalContext.current
            val placesClient = Places.createClient(context)
            val suggestions = remember { mutableStateListOf<AutocompletePrediction>() }
            var showDropdown by remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = direccionInput,
                    onValueChange = {
                        direccionInput = it
                        showDropdown = true

                        coroutineScope.launch {
                            val request = FindAutocompletePredictionsRequest.builder()
                                .setQuery(it.text)
                                .setCountries("SV")
                                .setTypeFilter(TypeFilter.ADDRESS)
                                .build()

                            placesClient.findAutocompletePredictions(request)
                                .addOnSuccessListener { response ->
                                    suggestions.clear()
                                    suggestions.addAll(response.autocompletePredictions)
                                }
                                .addOnFailureListener {
                                    Log.e("Places", "Error buscando sugerencias: ${it.message}")
                                }
                        }
                    },
                    placeholder = { Text("Buscar", fontFamily = abel, color = Color.Black) },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    leadingIcon = {
                        IconButton(onClick = {
                            if (direccionInput.text.isNotBlank()) {
                                coroutineScope.launch {
                                    val coords = getCoordinatesFromAddress(direccionInput.text)
                                    coords?.let {
                                        location = it
                                        markerState.position = it
                                        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 16f))
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
                            .padding(start = 4.dp, end = 4.dp, top = 6.dp, bottom = 8.dp)
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
                                        direccionInput = TextFieldValue(prediction.getFullText(null).toString())
                                        direccionConfirmada = prediction.getFullText(null).toString()
                                        showDropdown = false
                                        suggestions.clear()

                                        coroutineScope.launch {
                                            val coords = getCoordinatesFromAddress(direccionInput.text)
                                            coords?.let {
                                                location = it
                                                markerState.position = it
                                                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 16f))
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
                        cameraPositionState = cameraPositionState
                    ) {
                        LaunchedEffect(location) {
                            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(location, 16f))
                        }
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
                RedesSociales(R.drawable.instagram, instagram) { instagram = it }
                RedesSociales(R.drawable.facebook, facebook) { facebook = it }
                RedesSociales(R.drawable.tiktok, tiktok) { tiktok = it }
                RedesSociales(R.drawable.x, x) { x = it }
                RedesSociales(R.drawable.whatsapp, whatsapp) { whatsapp = it }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { if (camposValidos) onNext() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = camposValidos,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (camposValidos) Color(0xFF49724C) else Color(0xFFDFF2E1),
                    contentColor = if (camposValidos) Color.White else Color.Black.copy(alpha = 0.3f)
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
                        text = if (iconId == R.drawable.whatsapp) "Ingrese su número telefono" else "Ingrese su usuario",
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
            Log.e("GeocodingError", "Respuesta inválida: $response")
            null
        }
    } catch (e: Exception) {
        Log.e("GeocodingError", "No se pudo geocodificar: ${e.message}")
        null
    }
}