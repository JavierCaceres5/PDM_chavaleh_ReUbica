package com.poyecto.ReUbica.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class CategoriaItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    val textFieldState = remember { TextFieldState() }
    var searchResults by remember { mutableStateOf(listOf<String>()) }

    val categorias1 = listOf(
        CategoriaItem(Icons.Filled.LocalOffer, "Ropa") { /* Acción categoría de ropa */ },
        CategoriaItem(Icons.Filled.Fastfood, "Alimentos") { /* Acción categoría de alimentos */ },
        CategoriaItem(Icons.Filled.Restaurant, "Comida") { /* Acción categoría de comida */ },
        CategoriaItem(Icons.Filled.LocalLaundryService, "Higiene") { /* Acción categoría de higiene */ },
    )

    val categorias2 = listOf(
        CategoriaItem(Icons.Filled.Diamond, "Artesanías") { /* Acción categoría de artesanías */ },
        CategoriaItem(Icons.Filled.Book, "Librería") { /* Acción categoría de librería */ },
        CategoriaItem(Icons.Filled.Settings, "Servicios") { /* Acción categoría de servicios */ },
    )

    fun onSearch(query: String) {
        //Para la búsqueda cuando tengamos el backend
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(start = 25.dp, end = 25.dp, top = 10.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = Color(0xFFF7F8EF)
                ),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = textFieldState.text.toString(),
                        onQueryChange = {
                            textFieldState.edit { replace(0, length, it) }
                            expanded = true
                        },
                        onSearch = {
                            onSearch(textFieldState.text.toString())
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = {
                            Text(
                                "Locales, platos y productos",
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(Color(0xFFDFF2E1), shape = RoundedCornerShape(50))
                                    .clip(RoundedCornerShape(50))
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.Black
                                )
                            }
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    searchResults.forEach { result ->
                        ListItem(
                            headlineContent = { Text(result) },
                            modifier = Modifier
                                .clickable {
                                    textFieldState.edit {
                                        replace(0, length, result)
                                    }
                                    expanded = false
                                    onSearch(result)
                                }
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(25.dp)
        ) {
            Text(text = "Categorías", fontWeight = FontWeight.Bold, color = Color(0xFF5A3C1D))

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                categorias1.forEach { categoria ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
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
                        Text(
                            text = categoria.label,
                            fontSize = 12.sp,
                            color = Color.Black,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                categorias2.forEach { categoria ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
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
                        Text(
                            text = categoria.label,
                            fontSize = 12.sp,
                            color = Color.Black,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(0.5f))
            }
        }
    }
}
