package com.poyecto.ReUbica.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.poyecto.ReUbica.ui.Components.RestaurantCard
import com.proyecto.ReUbica.R

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
        Triple("Solemare", "Santa Ana", "Artesanías")
    )

    fun onSearch(query: String) {}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // SearchBar
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = SearchBarDefaults.colors(containerColor = Color(0xFFF7F8EF)),
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
                        Text("Locales, platos y productos", color = Color.Black, fontSize = 14.sp)
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(Color(0xFFDFF2E1), RoundedCornerShape(50))
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
        ) {}

        //  Categorías
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

        // Anuncio
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFDFF2E1))
                .padding(25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Desayunos desde $5.99", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5A3C1D))
                Spacer(Modifier.height(4.dp))
                Text("Descubre las mejores opciones para empezar bien tu día.", fontSize = 14.sp, color = Color(0xFF5A3C1D))
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Explorar locales", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.reubica),
                contentDescription = "Desayuno",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }

        // Mejores Restaurantes
        SeccionRestaurantes(
            titulo = "Los mejores restaurantes",
            destacados = destacados
        )

        // Según tus preferencias
        SeccionRestaurantes(
            titulo = "Según tus preferencias",
            destacados = destacados
        )
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
fun SeccionRestaurantes(titulo: String, destacados: List<Triple<String, String, String>>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF5A3C1D))
        Text("Ver más", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color(0xFF5A3C1D))
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(destacados.size) { index ->
            val (nombre, departamento, categoria) = destacados[index]
            RestaurantCard(
                nombre = nombre,
                departamento = departamento,
                categoria = categoria,
                imagenRes = R.drawable.reubica,
                isFavorito = false,
                onFavoritoClick = {},
                onVerTiendaClick = {}
            )
        }
    }
}
