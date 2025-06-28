package com.proyecto.ReUbica.ui.screens.ComercioScreen

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
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
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel

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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("chat_comercio/${business.nombre}/")
                },
                containerColor = Color(0xFF5A3C1D),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Email, contentDescription = "Chat")
            }
        },
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
                        .size(100.dp)
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
                    Spacer(Modifier.height(4.dp))
                    Text(
                        business.descripcion.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF5A3C1D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Text(
                            "8:00 AM - 5:00 PM",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5A3C1D)
                        )
                    }
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Text(
                            "Ubicación: ${business.direccion}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5A3C1D)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Icon(Icons.Default.Facebook, contentDescription = null)
                        Icon(Icons.Default.Email, contentDescription = null)
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(business.location, 15f)
                }

                GoogleMap(
                    modifier = Modifier
                        .width(140.dp)
                        .height(120.dp),
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
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(business.products) { product ->
                            ProductCard(
                                product = product.toProducto(),
                                favoritosViewModel = favoritosViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
