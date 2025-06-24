package com.poyecto.ReUbica.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.poyecto.ReUbica.ui.Components.RestaurantCard
import com.poyecto.ReUbica.ui.viewmodel.FavoritosViewModel
import com.proyecto.ReUbica.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController, favoritosViewModel: FavoritosViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf(listOf<String>()) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    val categorias1 = listOf(
        CategoriaItem(Icons.Filled.LocalOffer, "Ropa") {},
        CategoriaItem(Icons.Filled.Fastfood, "Alimentos") {},
        CategoriaItem(Icons.Filled.Restaurant, "Comida") {},
        CategoriaItem(Icons.Filled.LocalLaundryService, "Higiene") {},
    )

    val categorias2 = listOf(
        CategoriaItem(Icons.Filled.Diamond, "Artesanías") {},
        CategoriaItem(Icons.Filled.Book, "Librería") {},
        CategoriaItem(Icons.Filled.Settings, "Servicios") {},
    )

    val destacados = listOf(
        Triple("El Panalito", "Chinameca", "Alimentos"),
        Triple("Solemare", "Santa Ana", "Artesanías"),
        Triple("Pizza Ranch", "San Miguel", "Comida"),
        Triple("Arte Maya", "Ahuachapán", "Artesanías")
    )

    val resultadosFiltrados = destacados.filter {
        searchQuery.isBlank() ||
                it.first.contains(searchQuery, ignoreCase = true) ||
                it.second.contains(searchQuery, ignoreCase = true) ||
                it.third.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Search bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF7F8EF))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        debounceJob?.cancel()
                        debounceJob = coroutineScope.launch {
                            delay(2000)
                            if (it.isNotBlank() && it !in searchHistory) {
                                searchHistory = listOf(it) + searchHistory.take(4)
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Historial
        if (searchHistory.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                searchHistory.forEach { query ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = query,
                            modifier = Modifier.weight(1f),
                            fontSize = 14.sp
                        )
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.clickable {
                                searchHistory = searchHistory - query
                            },
                            tint = Color.Gray
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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

        SeccionRestaurantes(
            titulo = "Pueden interesarte",
            destacados = resultadosFiltrados,
            favoritosViewModel = favoritosViewModel,
            navController = navController
        )

        SeccionRestaurantes(
            titulo = "Nuevos emprendedores",
            destacados = resultadosFiltrados,
            favoritosViewModel = favoritosViewModel,
            navController = navController
        )
    }
}


data class CategoriaItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)
