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
import com.poyecto.ReUbica.ui.Components.RestaurantCard
import com.poyecto.ReUbica.ui.viewmodel.FavoritosViewModel
import com.proyecto.ReUbica.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(favoritosViewModel: FavoritosViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf(listOf<String>()) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

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
                    modifier = Modifier.weight(1f)
                )
                if (searchQuery.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { searchQuery = "" },
                        tint = Color.Black
                    )
                }
            }
        }

        if (searchHistory.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                searchHistory.forEach { query ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp).padding(bottom = 6.dp),
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

        SeccionRestaurantes(
            titulo = "Pueden interesarte",
            destacados = resultadosFiltrados,
            favoritosViewModel = favoritosViewModel
        )

        SeccionRestaurantes(
            titulo = "Nuevos emprendedores",
            destacados = resultadosFiltrados,
            favoritosViewModel = favoritosViewModel
        )
    }
}


data class CategoriaItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)
