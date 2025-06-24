package com.poyecto.ReUbica.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.poyecto.ReUbica.ui.viewmodel.BusinessViewModel
import androidx.compose.foundation.layout.Row
import com.poyecto.ReUbica.ui.Components.ProductCard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavHostController

@Composable
fun DetallesComercioScreen(
    navController: NavHostController,
    nombre: String,
    departamento: String,
    categoria: String,
    businessViewModel: BusinessViewModel = viewModel()
) {
    val business by businessViewModel.business.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(business.name, style = MaterialTheme.typography.titleLarge)
        Text(business.description, style = MaterialTheme.typography.bodyMedium)
        Text(business.hours)
        Text(business.locationName)

        Spacer(Modifier.height(8.dp))

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(business.location, 15f)
        }

        GoogleMap(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(state = MarkerState(business.location))
        }

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Filled.Favorite, contentDescription = null)
            Icon(Icons.Filled.Share, contentDescription = null)
            Icon(Icons.Filled.AccountCircle, contentDescription = null)
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(business.products) { product ->
                ProductCard(product)
            }
        }
    }
}
