package com.proyecto.ReUbica.ui.screens.ProductoScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SettingsPhone
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.proyecto.ReUbica.data.model.review.ReviewModel

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavHostController,
    token: String,
    emprendimientoID: String,
    viewModel: ProductoViewModel = viewModel(),
    reviewViewModel: ReviewViewModel = viewModel()
) {
    val productos by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val emprendimientoReviews by reviewViewModel.emprendimientoReviews.collectAsState()
    val reviewLoading by reviewViewModel.loading.collectAsState()
    val reviewError by reviewViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProductosByEmprendimiento(token, emprendimientoID)
    }

    LaunchedEffect(emprendimientoID) {
        reviewViewModel.getReviewsByEmprendimiento(token, emprendimientoID)
    }

    if (loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF5A3C1D))
        }
        return
    }
    if (error != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $error", color = Color.Red, fontWeight = FontWeight.Bold)
        }
        return
    }

    val product = productos.find { it.id.toString() == productId }
    if (product == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Producto no encontrado", color = Color.Red, fontWeight = FontWeight.Bold)
        }
        return
    }

    val filteredReviews: List<ReviewModel> = emprendimientoReviews
        .find { it.productoID.toString() == productId }
        ?.valoraciones ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        AsyncImage(
            model = product.product_image ?: "https://via.placeholder.com/150",
            contentDescription = product.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(product.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF5A3C1D))

        Spacer(modifier = Modifier.height(16.dp))

        Text(product.descripcion, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF5A3C1D))

        Spacer(modifier = Modifier.height(8.dp))

        Text("$${product.precio}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF5A3C1D))

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Column(modifier = Modifier.fillMaxWidth(0.6f)) {
                InfoRow(Icons.Outlined.SettingsPhone, "8874-7387")
                InfoRow(Icons.Outlined.Schedule, "Horario 2-5 pm")
                InfoRow(Icons.Outlined.LocationOn, "Universidad José Simeón Cañas")
                InfoRow(Icons.Outlined.Email, "correo@ejemplo.com")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFF5A3C1D), CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ya casi", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D4F30))
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Divider(color = Color(0xFF5A3C1D), thickness = 2.dp)
        Spacer(modifier = Modifier.height(8.dp))

        Text("💡 Comentarios de usuarios", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D4F30))

        if (reviewLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF5A3C1D))
            }
        } else if (reviewError != null) {
            Text(
                "Error al cargar comentarios: $reviewError",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (filteredReviews.isEmpty()) {
            Text(
                "Aún no hay reseñas de este producto.",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredReviews) { review ->
                    ReviewItem(review)
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(6.dp))
        Text(text = text, color = Color(0xFF5A3C1D))
    }
}

@Composable
fun ReviewItem(review: ReviewModel) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        AsyncImage(
            model = "https://via.placeholder.com/150",
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(30.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("Usuario", fontWeight = FontWeight.Bold, color = Color(0xFF5A3C1D))
            Row {
                repeat(review.rating.toInt()) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(15.dp))
                }
                repeat(5 - review.rating.toInt()) {
                    Icon(Icons.Default.StarOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(15.dp))
                }
            }
            Text(review.comentario, color = Color(0xFF5A3C1D))
        }
    }
}
