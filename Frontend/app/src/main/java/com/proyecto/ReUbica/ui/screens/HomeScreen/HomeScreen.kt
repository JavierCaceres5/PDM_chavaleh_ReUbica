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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.Components.RestaurantCard
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel
import com.proyecto.ReUbica.ui.screens.SearchScreen.CategoriaItem
import java.text.SimpleDateFormat
import java.util.*
import com.proyecto.ReUbica.data.local.UserSessionManager
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.proyecto.ReUbica.utils.LocationUtils
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date



@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    favoritosViewModel: FavoritosViewModel = viewModel(),
    homeViewModel: HomeScreenViewModel = viewModel()
) {
    val resultados by homeViewModel.resultadosByCategory.collectAsState()
    val loading by homeViewModel.loading.collectAsState()
    val error by homeViewModel.error.collectAsState()
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager(context) }
    val todosLosEmprendimientos by homeViewModel.todosLosEmprendimientos.collectAsState()
    var userToken by remember { mutableStateOf<String?>(null) }
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    var userLocation by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            val location = LocationUtils.getCurrentLocation(context)
            userLocation = location
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    val userLat = userLocation?.latitude ?: 0.0
    val userLon = userLocation?.longitude ?: 0.0


    LaunchedEffect(Unit) {
        userToken = userSessionManager.getToken()
        userToken?.let {
            homeViewModel.obtenerTodosLosEmprendimientos(it)
        }
    }

    val categorias1 = listOf(
        CategoriaItem(Icons.Filled.LocalOffer, "Ropa") {
            categoriaSeleccionada = "Ropa"
            homeViewModel.searchEmprendimientoByCategory("Ropa")
        },
        CategoriaItem(Icons.Filled.Fastfood, "Alimentos") {
            categoriaSeleccionada = "Alimentos"
            homeViewModel.searchEmprendimientoByCategory("Alimentos")
        },
        CategoriaItem(Icons.Filled.Restaurant, "Comida") {
            categoriaSeleccionada = "Comida"
            homeViewModel.searchEmprendimientoByCategory("Comida")
        },
        CategoriaItem(Icons.Filled.LocalLaundryService, "Higiene") {
            categoriaSeleccionada = "Higiene"
            homeViewModel.searchEmprendimientoByCategory("Higiene")
        },
    )

    val categorias2 = listOf(
        CategoriaItem(Icons.Filled.Diamond, "Artesanías") {
            categoriaSeleccionada = "Artesanias"
            homeViewModel.searchEmprendimientoByCategory("Artesanias")
        },
        CategoriaItem(Icons.Filled.Book, "Librería") {
            categoriaSeleccionada = "Librería"
            homeViewModel.searchEmprendimientoByCategory("Libreria")
        },
        CategoriaItem(Icons.Filled.Settings, "Servicios") {
            categoriaSeleccionada = "Servicios"
            homeViewModel.searchEmprendimientoByCategory("Servicios")
        },
    )

    fun isNearby(
        userLat: Double, userLon: Double,
        placeLat: Double?, placeLon: Double?,
        radiusKm: Double = 5.0
    ): Boolean {
        if (placeLat == null || placeLon == null) return false
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(placeLat - userLat)
        val dLon = Math.toRadians(placeLon - userLon)
        val lat1 = Math.toRadians(userLat)
        val lat2 = Math.toRadians(placeLat)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = earthRadiusKm * c

        return distance <= radiusKm
    }

    val comerciosCercanos = todosLosEmprendimientos.filter {
        isNearby(userLat, userLon, it.latitud, it.longitud)
    }.map {
        Triple(
            it.nombre ?: "Sin nombre",
            it.direccion ?: "Sin dirección",
            it.categoriasPrincipales.firstOrNull() ?: "Sin categoría"
        )
    }

    fun parseFecha(fecha: String): Date? {
        return try {
            val zonedDateTime = ZonedDateTime.parse(fecha, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            Date.from(zonedDateTime.toInstant())
        } catch (e: Exception) {
            println("❌ Error parseando fecha: $fecha")
            e.printStackTrace()
            null
        }
    }



    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val now = Date()
    val oneWeekMillis = 7 * 24 * 60 * 60 * 1000

    val nuevosEmprendimientos = todosLosEmprendimientos.filter {
        it.created_at?.let { fecha ->
            try {
                val createdDate = parseFecha(fecha)
                createdDate != null && now.time - createdDate.time <= oneWeekMillis
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } ?: false
    }.map {
        Triple(
            it.nombre ?: "Sin nombre",
            it.direccion ?: "Sin dirección",
            it.categoriasPrincipales?.firstOrNull() ?: "Sin categoría"
        )
    }


    LaunchedEffect(todosLosEmprendimientos) {
        val now = Date()
        val oneWeekMillis = 7 * 24 * 60 * 60 * 1000

        println("====== DEPURACIÓN DE NUEVOS EMPRENDIMIENTOS ======")
        todosLosEmprendimientos.forEach { emp ->
            emp.created_at?.let { fecha ->
                val parsed = parseFecha(fecha)
                val diff = parsed?.let { now.time - it.time }
                val isNew = diff != null && diff <= oneWeekMillis
                println("Nombre: ${emp.nombre}")
                println("Fecha Original: $fecha")
                println("Fecha Parseada: $parsed")
                println("Diff (ms): $diff")
                println("Es nuevo?: $isNew")
                println("------------------------------------")
            }
        }
        println("======================================")
    }




    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text("Categorías", fontWeight = FontWeight.Bold, color = Color(0xFF5A3C1D))
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

        // Nueva sección: Resultados por categoría como carrusel horizontal
        if (resultados.isNotEmpty()) {
            item {
                SeccionRestaurantes("Descubrimientos por categoría", resultados.map {
                    Triple(
                        it.nombre ?: "Sin nombre",
                        it.direccion ?: "Sin dirección",
                        it.categoriasPrincipales.firstOrNull() ?: "Sin categoría"
                    )
                }, favoritosViewModel, navController)
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

@Composable
fun SeccionRestaurantes(
    titulo: String,
    destacados: List<Triple<String, String, String>>,
    favoritosViewModel: FavoritosViewModel,
    navController: NavHostController
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF5A3C1D))
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(destacados.size) { index ->
                val (nombre, departamento, categoria) = destacados[index]
                val isFavorito = favoritosViewModel.isFavoritoComercio(nombre)

                RestaurantCard(
                    nombre = nombre,
                    departamento = departamento,
                    categoria = categoria,
                    imagenRes = R.drawable.reubica,
                    isFavorito = isFavorito,
                    onFavoritoClick = {
                        favoritosViewModel.toggleFavoritoComercio(
                            nombre = nombre,
                            departamento = departamento,
                            categoria = categoria
                        )
                    },
                    onVerTiendaClick = {
                        navController.navigate(
                            ComercioNavigation(
                                id = "1",
                                nombre = nombre,
                                descripcion = "Comercio destacado de la zona",
                                categoria = categoria,
                                direccion = departamento,
                                latitud = 13.6989,
                                longitud = -89.1914,
                                horario = "9:00 AM - 9:00 PM"
                            )
                        )
                    }
                )
            }
        }
    }
}
