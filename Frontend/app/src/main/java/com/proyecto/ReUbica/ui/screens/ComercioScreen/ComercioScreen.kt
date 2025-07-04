package com.proyecto.ReUbica.ui.screens.ComercioScreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.ui.Components.ProductCard
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel
import com.proyecto.ReUbica.R
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComercioScreen(
    navController: NavHostController,
    navArgs: EmprendimientoModel,
    businessViewModel: ComercioViewModel = viewModel()
) {
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager(context) }

    val business by businessViewModel.business.collectAsState()
    val favoritosViewModel: FavoritosViewModel = viewModel()
    val markerState = remember { MarkerState(position = business.location) }


    LaunchedEffect(Unit) {
        businessViewModel.setBusinessInfo(navArgs, userSessionManager)
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEAEAEA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Store,
                        contentDescription = "Logo del comercio",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        business.nombre.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF5A3C1D),
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        business.descripcion.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF5A3C1D)
                    )

                    Spacer(Modifier.height(5.dp))

                    Row {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            "Contacto: ${business.emprendimientoPhone}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5A3C1D),
                            textAlign = TextAlign.Justify
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    Row {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.padding(end = 4.dp)

                        )
                        Text(
                            "Ubicación: ${business.direccion}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5A3C1D),
                            textAlign = TextAlign.Justify
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                val instagramUrl = business.redes_sociales?.Instagram
                if (!instagramUrl.isNullOrBlank()) {
                    Icon(
                        painter = painterResource(id = R.drawable.instagram),
                        contentDescription = "Instagram",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val uri = instagramUrl.toUri()
                                val webIntent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(webIntent)
                            }
                    )
                }

                val facebookUrl = business.redes_sociales?.Facebook
                if (!facebookUrl.isNullOrBlank()) {
                    Icon(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = "Facebook",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val uri = facebookUrl.toUri()
                                val webIntent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(webIntent)
                            }
                    )
                }

                val tiktokUrl = business.redes_sociales?.TikTok
                if (!tiktokUrl.isNullOrBlank()) {
                    Icon(
                        painter = painterResource(id = R.drawable.tiktok),
                        contentDescription = "Tiktok",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val uri = tiktokUrl.toUri()
                                val webIntent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(webIntent)
                            }
                    )
                }

                val twitterUrl = business.redes_sociales?.Twitter
                if (!twitterUrl.isNullOrBlank()) {
                    Icon(
                        painter = painterResource(id = R.drawable.x),
                        contentDescription = "X",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val uri = twitterUrl.toUri()
                                val webIntent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(webIntent)
                            }
                    )
                }




            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                Spacer(modifier = Modifier.width(8.dp))

                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(business.location, 15f)
                }

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(state = markerState)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                business.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                business.error != null -> {
                    Text(
                        "Error: ${business.error}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                business.products.isEmpty() -> {
                    Text(
                        "Este emprendimiento aún no tiene productos para mostrar.",
                        color = Color(0xFF5A3C1D),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                    )
                }
                else -> {
                    val tokenState = remember { mutableStateOf("") }
                    val emprendimientoID = business.id?.toString() ?: ""

                    LaunchedEffect(Unit) {
                        val token = userSessionManager.getToken() ?: ""
                        tokenState.value = token
                    }

                    if (tokenState.value.isNotEmpty() && emprendimientoID.isNotEmpty()) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(business.products) { product ->
                                ProductCard(
                                    product = product,
                                    favoritosViewModel = favoritosViewModel,
                                    navController = navController,
                                    token = tokenState.value,
                                    emprendimientoID = emprendimientoID
                                )
                            }
                        }
                    } else {
                        Text(
                            "Cargando datos de sesión...",
                            color = Color(0xFF5A3C1D),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
