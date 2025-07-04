package com.proyecto.ReUbica.ui.Components

import android.net.Uri
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.proyecto.ReUbica.data.model.producto.ProductoResponse
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel

@Composable
fun ProductCard(
    product: ProductoResponse,
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
                val encodedToken = Uri.encode(token)
                val encodedEmprendimientoID = Uri.encode(emprendimientoID)
                navController.navigate("product_detail/${product.id}?token=$encodedToken&emprendimientoID=$encodedEmprendimientoID")
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = product.product_image,
                contentDescription = "Imagen del producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    product.nombre.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    product.descripcion.toString(),
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
                                nombre = product.nombre.toString(),
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
                }
            }
        }
    }
}
