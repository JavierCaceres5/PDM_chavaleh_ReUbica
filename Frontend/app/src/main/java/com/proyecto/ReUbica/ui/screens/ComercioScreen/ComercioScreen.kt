package com.proyecto.ReUbica.ui.screens.ComercioScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavArgs
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.proyecto.ReUbica.ui.Components.ProductCard
import com.proyecto.ReUbica.ui.layouts.BottomBar
import com.proyecto.ReUbica.ui.layouts.TopBar
import com.proyecto.ReUbica.ui.layouts.navItem
import com.proyecto.ReUbica.ui.screens.ComercioScreen.ComercioViewModel
import com.proyecto.ReUbica.ui.navigations.*
import androidx.compose.foundation.lazy.items
import com.proyecto.ReUbica.data.api.DummyProduct
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel


@Composable
fun ComercioScreen(
    navController: NavHostController,
    navArgs: ComercioNavigation,
    businessViewModel: ComercioViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        businessViewModel.setBusinessInfo(navArgs)
    }
    val business by businessViewModel.business.collectAsState()
    val favoritosViewModel: FavoritosViewModel = viewModel()
    var selectedItem by rememberSaveable { mutableStateOf("nowplaying") }
    val markerState = remember { MarkerState(position = business.location) }

    val navItems = listOf(
        navItem("Inicio", Icons.Filled.Home, "nowplaying"),
        navItem("Buscar", Icons.Default.Search, "search"),
        navItem("Mi cuenta", Icons.Filled.AccountCircle, "account"),
        navItem("Favoritos", Icons.Default.Favorite, "favorites")
    )

    fun onItemSelected(currentItem: String) {
        selectedItem = currentItem
        when (currentItem) {
            "nowplaying" -> navController.navigate(HomeScreenNavigation)
            "search" -> navController.navigate(SearchScreenNavigation)
            "account" -> navController.navigate(ProfileScreenNavigation)
            "favorites" -> navController.navigate(FavoritesScreenNavigation)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    println("Chatear con el comercio")
                },
                containerColor = Color(0xFF5A3C1D),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Email, contentDescription = "Chat")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
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
                    Text(business.name, style = MaterialTheme.typography.titleLarge, color = Color(0xFF5A3C1D), fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(4.dp))
                    Text(business.description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fila: Horario, ubicación, redes y mapa
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)
                    .padding(horizontal = 5.dp)) {
                    Row {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = Color.Black)
                        Text(" ${business.hours}", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF5A3C1D))
                    }

                    Row {
                        Icon(imageVector = Icons.Default.LocationOn, tint = Color.Black, contentDescription = null)
                        Text("Ubicación: ${business.locationName}", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF5A3C1D))
                    }

                    Row(modifier = Modifier.padding(top = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
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

            LazyColumn {
                items(business.products) { product ->
                    ProductCard(product, favoritosViewModel)
                }
            }
        }
    }

}