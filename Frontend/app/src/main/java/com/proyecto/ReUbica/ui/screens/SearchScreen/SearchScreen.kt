package com.proyecto.ReUbica.ui.screens.SearchScreen

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.ui.screens.EmprendimientosBuscar.SearchScreenViewModel
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel
import com.proyecto.ReUbica.ui.screens.HomeScreen.HomeScreenViewModel
import com.proyecto.ReUbica.ui.screens.HomeScreen.SeccionRestaurantes
import com.proyecto.ReUbica.utils.LocationUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    favoritosViewModel: FavoritosViewModel = viewModel(),
    searchViewModel: SearchScreenViewModel = viewModel(),
    homeViewModel: HomeScreenViewModel = viewModel()
) {
    val resultadosApi by searchViewModel.resultadosByNombre.collectAsState()
    val loading by searchViewModel.loading.collectAsState()
    val error by searchViewModel.error.collectAsState()

    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager(context) }
    val todosLosEmprendimientos by homeViewModel.todosLosEmprendimientos.collectAsState()

    var userLocation by remember { mutableStateOf<Location?>(null) }
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            userLocation = LocationUtils.getCurrentLocation(context)
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        userSessionManager.getToken()?.let { homeViewModel.obtenerTodosLosEmprendimientos(it) }
        searchViewModel.setUserSessionManager(userSessionManager)
    }

    var searchQuery by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf(listOf<String>()) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    fun isNearby(userLat: Double, userLon: Double, placeLat: Double?, placeLon: Double?, radiusKm: Double = 5.0): Boolean {
        if (placeLat == null || placeLon == null) return false
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(placeLat - userLat)
        val dLon = Math.toRadians(placeLon - userLon)
        val lat1 = Math.toRadians(userLat)
        val lat2 = Math.toRadians(placeLat)
        val a = Math.sin(dLat / 2).pow(2) + Math.sin(dLon / 2).pow(2) * Math.cos(lat1) * Math.cos(lat2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = earthRadiusKm * c
        return distance <= radiusKm
    }

    val userLat = userLocation?.latitude ?: 0.0
    val userLon = userLocation?.longitude ?: 0.0

    val comerciosCercanos = todosLosEmprendimientos.filter {
        isNearby(userLat, userLon, it.latitud, it.longitud)
    }

    fun parseFecha(fecha: String): Date? {
        return try {
            val zonedDateTime = ZonedDateTime.parse(fecha, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            Date.from(zonedDateTime.toInstant())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val now = Date()
    val oneWeekMillis = 7 * 24 * 60 * 60 * 1000

    val nuevosEmprendimientos = todosLosEmprendimientos.filter {
        it.created_at?.let { fecha ->
            parseFecha(fecha)?.let { createdDate ->
                now.time - createdDate.time <= oneWeekMillis
            } ?: false
        } ?: false
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFF7F8EF))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            debounceJob?.cancel()
                            debounceJob = coroutineScope.launch {
                                delay(500)
                                if (query.isNotBlank()) {
                                    if (query !in searchHistory) {
                                        searchHistory = listOf(query) + searchHistory.take(4)
                                    }
                                    searchViewModel.searchEmprendimientoByNombre(query)
                                }
                            }
                        },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                        modifier = Modifier.weight(1f)
                    )
                    if (searchQuery.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Borrar",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { searchQuery = "" },
                            tint = Color.Black
                        )
                    }
                }
            }
        }

        if (searchHistory.isNotEmpty()) {
            item {
                Column {
                    searchHistory.forEach { query ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    searchQuery = query
                                    searchViewModel.searchEmprendimientoByNombre(query)
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = query, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Eliminar",
                                modifier = Modifier.clickable { searchHistory = searchHistory - query },
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        when {
            loading -> {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                    )
                }
            }

            !error.isNullOrEmpty() -> {
                item {
                    Text(
                        text = "Error: $error",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            resultadosApi.isNotEmpty() -> {
                item {
                    SeccionRestaurantes(
                        titulo = "Resultados de búsqueda",
                        emprendimientos = resultadosApi,
                        favoritosViewModel = favoritosViewModel,
                        navController = navController
                    )
                }
            }

            searchQuery.isBlank() -> {
                if (nuevosEmprendimientos.isNotEmpty()) {
                    item {
                        SeccionRestaurantes(
                            titulo = "Nuevos en la plataforma",
                            emprendimientos = nuevosEmprendimientos,
                            favoritosViewModel = favoritosViewModel,
                            navController = navController
                        )
                    }
                }
                if (comerciosCercanos.isNotEmpty()) {
                    item {
                        SeccionRestaurantes(
                            titulo = "Locales cerca de ti",
                            emprendimientos = comerciosCercanos,
                            favoritosViewModel = favoritosViewModel,
                            navController = navController
                        )
                    }
                }
            }

            searchQuery.isNotBlank() && resultadosApi.isEmpty() -> {
                item {
                    Text(
                        text = "No se encontraron resultados para \"$searchQuery\".",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

data class CategoriaItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val onClick: () -> Unit
)
