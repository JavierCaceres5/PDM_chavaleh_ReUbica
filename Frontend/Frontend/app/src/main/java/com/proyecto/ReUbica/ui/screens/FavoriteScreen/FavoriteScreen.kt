package com.proyecto.ReUbica.ui.screens.FavoriteScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.Components.RestaurantCard
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.Favorito

@Composable
fun FavoriteScreen(favoritosViewModel: FavoritosViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf("Puestos") }
    var searchQuery by remember { mutableStateOf("") }

    val favoritos = favoritosViewModel.favoritos

    val comercios = favoritos.filterIsInstance<Favorito.Comercio>()
    val productos = favoritos.filterIsInstance<Favorito.Producto>()

    val comerciosFiltrados = if (searchQuery.isBlank()) {
        comercios
    } else {
        comercios.filter {
            it.nombre.contains(searchQuery, ignoreCase = true) ||
                    it.departamento.contains(searchQuery, ignoreCase = true) ||
                    it.categoria.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tus favoritos", fontSize = 28.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por nombre, categoría o localidad", fontSize = 14.sp, color = Color.DarkGray) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = Color.DarkGray) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF7F8EF),
                focusedContainerColor = Color(0xFFF7F8EF),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                FavoriteTab("Productos", selectedTab == "Productos") {
                    selectedTab = "Productos"
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                FavoriteTab("Puestos", selectedTab == "Puestos") {
                    selectedTab = "Puestos"
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == "Puestos") {
            if (comerciosFiltrados.isEmpty()) {
                Text("Aún no tienes puestos guardados.", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(comerciosFiltrados.size) { index ->
                        val favorito = comerciosFiltrados[index]
                        RestaurantCard(
                            nombre = favorito.nombre,
                            departamento = favorito.departamento,
                            categoria = favorito.categoria,
                            imagenRes = R.drawable.reubica,
                            isFavorito = true,
                            onFavoritoClick = {
                                favoritosViewModel.toggleFavoritoComercio(
                                    favorito.nombre,
                                    favorito.departamento,
                                    favorito.categoria
                                )
                            },
                            onVerTiendaClick = {}
                        )
                    }
                }
            }
        } else {
            if (productos.isEmpty()) {
                Text("Aún no tienes productos guardados.", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(productos.size) { index ->
                        val producto = productos[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8EF)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(producto.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("Precio: \$${producto.precio}", fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = {
                                    favoritosViewModel.toggleFavoritoProducto(
                                        producto.id,
                                        producto.nombre,
                                        producto.precio
                                    )
                                }) {
                                    Text("Eliminar de favoritos")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteTab(title: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) Color(0xFFDFF2E1) else Color(0xFFF7F8EF)
    val icon = if (title == "Productos") Icons.Filled.ShoppingBag else Icons.Filled.Store

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable { onClick() }
            .width(200.dp).height(45.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Black)
        Spacer(modifier = Modifier.width(8.dp))
        Text(title)
    }
}
