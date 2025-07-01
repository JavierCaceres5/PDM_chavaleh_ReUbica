package com.proyecto.ReUbica.ui.screens.HomeScreen

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.ui.Components.RestaurantCard
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel
import com.proyecto.ReUbica.ui.screens.SearchScreen.CategoriaItem
import com.proyecto.ReUbica.utils.LocationUtils
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    favoritosViewModel: FavoritosViewModel = viewModel(),
    homeViewModel: HomeScreenViewModel = viewModel()
) {
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager(context) }
    val scope = rememberCoroutineScope()

    val resultados by homeViewModel.resultadosByCategory.collectAsState()
    val loading by homeViewModel.loading.collectAsState()
    val error by homeViewModel.error.collectAsState()
    val todosLosEmprendimientos by homeViewModel.todosLosEmprendimientos.collectAsState()

    var userLocation by remember { mutableStateOf<Location?>(null) }
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        if (locationPermissionState.status.isGranted) {
            userLocation = LocationUtils.getCurrentLocation(context)
        } else {
            locationPermissionState.launchPermissionRequest()
        }

        val token = userSessionManager.getToken()
        token?.let { homeViewModel.obtenerTodosLosEmprendimientos(it) }
    }

    val categorias1 = listOf(
        CategoriaItem(Icons.Filled.LocalOffer, "Ropa") { homeViewModel.searchEmprendimientoByCategory("Ropa") },
        CategoriaItem(Icons.Filled.Fastfood, "Alimentos") { homeViewModel.searchEmprendimientoByCategory("Alimentos") },
        CategoriaItem(Icons.Filled.Restaurant, "Comida") { homeViewModel.searchEmprendimientoByCategory("Comida") },
        CategoriaItem(Icons.Filled.LocalLaundryService, "Higiene") { homeViewModel.searchEmprendimientoByCategory("Higiene") },
    )

    val categorias2 = listOf(
        CategoriaItem(Icons.Filled.Diamond, "Artesanías") { homeViewModel.searchEmprendimientoByCategory("Artesanias") },
        CategoriaItem(Icons.Filled.Book, "Librería") { homeViewModel.searchEmprendimientoByCategory("Libreria") },
        CategoriaItem(Icons.Filled.Settings, "Servicios") { homeViewModel.searchEmprendimientoByCategory("Servicios") }
    )

    val userLat = userLocation?.latitude ?: 0.0
    val userLon = userLocation?.longitude ?: 0.0

    fun isNearby(lat1: Double, lon1: Double, lat2: Double?, lon2: Double?, radiusKm: Double = 5.0): Boolean {
        if (lat2 == null || lon2 == null) return false
        val earthRadius = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2).pow(2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2).pow(2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c <= radiusKm
    }

    val comerciosCercanos by remember(todosLosEmprendimientos, userLocation) {
        mutableStateOf(
            todosLosEmprendimientos.filter {
                isNearby(userLat, userLon, it.latitud, it.longitud)
            }
        )
    }

    val nuevosEmprendimientos by remember(todosLosEmprendimientos) {
        mutableStateOf(
            todosLosEmprendimientos.filter { emp ->
                emp.created_at?.let {
                    val date = try {
                        Date.from(ZonedDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant())
                    } catch (_: Exception) { null }
                    date?.let { d -> Date().time - d.time <= 7 * 24 * 60 * 60 * 1000 }
                } ?: false
            }
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text("Categorías", fontWeight = FontWeight.ExtraBold, color = Color(0xFF5A3C1D),
                    modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, bottom = 0.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categorias1.forEach {
                        CategoriaBox(it, Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Spacer(modifier = Modifier.weight(0.5f))
                    categorias2.forEach {
                        CategoriaBox(it, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.weight(0.5f))
                }
            }
        }

        if (resultados.isNotEmpty()) {
            item {
                SeccionRestaurantes("Descubrimientos por categoría", resultados, favoritosViewModel, navController)
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFDFF2E1))
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Desayunos desde $5.99", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5A3C1D))
                    Spacer(Modifier.height(4.dp))
                    Text("Descubre las mejores opciones para empezar bien tu día.", fontSize = 13.sp, color = Color(0xFF5A3C1D), textAlign = TextAlign.Justify)
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                        shape = RoundedCornerShape(40)
                    ) {
                        Text("Explorar locales", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.reubica),
                    contentDescription = "Desayuno",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
        }

        if (comerciosCercanos.isNotEmpty()) {
            item {
                SeccionRestaurantes("Locales cerca de ti", comerciosCercanos, favoritosViewModel, navController)
            }
        }

        if (nuevosEmprendimientos.isNotEmpty()) {
            item {
                SeccionRestaurantes("Nuevos en la plataforma", nuevosEmprendimientos, favoritosViewModel, navController)
            }
        }

        if (loading) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }

        if (!error.isNullOrEmpty()) {
            item {
                Text(error ?: "", color = Color.Red, modifier = Modifier.padding(16.dp))
            }
        }
    }
}


fun isValidUrl(url: String?): Boolean =
    url != null && (url.startsWith("http://") || url.startsWith("https://"))

@Composable
fun SeccionRestaurantes(
    titulo: String,
    emprendimientos: List<EmprendimientoModel>,
    favoritosViewModel: FavoritosViewModel,
    navController: NavHostController
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            titulo,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF5A3C1D),
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(emprendimientos) { emprendimiento ->

                val logoUrl = if (isValidUrl(emprendimiento.logo)) emprendimiento.logo else null

                RestaurantCard(
                    nombre = emprendimiento.nombre ?: "Sin nombre",
                    departamento = emprendimiento.direccion ?: "Sin dirección",
                    categoria = emprendimiento.categoriasPrincipales.firstOrNull() ?: "Sin categoría",
                    imagenRes = logoUrl,
                    isFavorito = favoritosViewModel.isFavoritoComercio(emprendimiento.nombre ?: ""),
                    onFavoritoClick = {
                        favoritosViewModel.toggleFavoritoComercio(
                            nombre = emprendimiento.nombre ?: "",
                            departamento = emprendimiento.direccion ?: "",
                            categoria = emprendimiento.categoriasPrincipales.firstOrNull() ?: "",
                            logo = emprendimiento.logo ?: ""
                        )
                    },
                    onVerTiendaClick = {
                        navController.navigate(
                            ComercioNavigation(
                                id = emprendimiento.id.toString(),
                                nombre = emprendimiento.nombre ?: "Sin nombre",
                                descripcion = emprendimiento.descripcion ?: "Sin descripción",
                                categoria = emprendimiento.categoriasPrincipales.firstOrNull() ?: "Sin categoría",
                                direccion = emprendimiento.direccion ?: "Sin dirección",
                                latitud = emprendimiento.latitud ?: 0.0,
                                longitud = emprendimiento.longitud ?: 0.0,
                                horario = "8:00 AM - 5:00 PM"
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun CategoriaBox(categoria: CategoriaItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { categoria.onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFDFF2E1), shape = RoundedCornerShape(25))
                .size(50.dp)
                .clip(RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = categoria.icon,
                contentDescription = categoria.label,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = categoria.label, fontSize = 12.sp, color = Color.Black)
    }
}

