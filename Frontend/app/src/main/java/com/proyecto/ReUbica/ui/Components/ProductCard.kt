package com.proyecto.ReUbica.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

import com.proyecto.ReUbica.data.model.DummyProduct

import com.proyecto.ReUbica.data.model.producto.ProductoModel

import com.proyecto.ReUbica.ui.navigations.ProductDetailNavigation
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel

@Composable
fun ProductCard(
    product: ProductoModel,
    favoritosViewModel: FavoritosViewModel = viewModel(),
    navController: NavHostController,
    token: String,
    emprendimientoID: String
) {
    val isFavorito = favoritosViewModel.isFavoritoProducto(product.id.toString())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navController.navigate(
                    ProductDetailNavigation.withArgs(
                        product.id.toString(),
                        token,
                        emprendimientoID
                    )
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray)
            )

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    product.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    product.descripcion,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF5A3C1D)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$${product.precio}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5A3C1D)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = if (isFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = Color(0xFF5A3C1D),
                        modifier = Modifier.clickable {
                            favoritosViewModel.toggleFavoritoProducto(
                                id = product.id.toString(),
                                nombre = product.nombre,
                                precio = product.precio
                            )
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Calificación",
                        tint = Color(0xFF5A3C1D)
                    )
                    //Text(
                    //    text = product.rating.toString(),
                    //    color = Color(0xFF5A3C1D)
                    //)
                }
            }
        }
    }
}
