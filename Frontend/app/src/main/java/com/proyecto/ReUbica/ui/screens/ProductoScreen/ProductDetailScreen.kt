package com.proyecto.ReUbica.ui.screens.ProductoScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavHostController,
    token: String,
    emprendimientoID: String,
    viewModel: ProductoViewModel = viewModel()
) {
    val productos by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProductosByEmprendimiento(token, emprendimientoID)
    }

    when {
        loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF5A3C1D))
            }
            return
        }
        error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = error ?: "Error desconocido",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
            return
        }
    }

    val product = productos.find { it.id.toString() == productId }

    if (product == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Producto no encontrado", color = Color.Red, fontWeight = FontWeight.Bold)
        }
        return
    }

    var comentario by remember { mutableStateOf("") }

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

        Text(
            text = product.nombre,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A3C1D)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(product.descripcion, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF5A3C1D))

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "$${product.precio}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF5A3C1D)
        )

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
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Ya casi",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4F30)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Divider(color = Color(0xFF5A3C1D), thickness = 2.dp)

        Spacer(modifier = Modifier.height(8.dp))

        Text("💡 Comentarios de usuarios", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D4F30))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .fillMaxWidth(0.5f)
            ) {
                val comentarios = listOf(
                    Triple("https://randomuser.me/api/portraits/women/44.jpg", "Fernanda", 4),
                    Triple("https://randomuser.me/api/portraits/men/45.jpg", "Jorge", 1)
                )

                items(comentarios.size) { index ->
                    val (imageUrl, nombre, rating) = comentarios[index]
                    UserComment(imageUrl, nombre, rating)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Escribe tu comentario") },
                    textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.End)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        "Envía tu comentario",
                        color = Color(0xFF5A3C1D),
                        fontWeight = FontWeight.Bold
                    )
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
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun UserComment(imageUrl: String, nombre: String, rating: Int) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(30.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(nombre, fontWeight = FontWeight.Bold, color = Color(0xFF5A3C1D))
            Row {
                repeat(rating) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(15.dp))
                }
                repeat(5 - rating) {
                    Icon(Icons.Default.StarOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(15.dp))
                }
            }
            Text(
                if (nombre == "Fernanda") "Me pareció increíble" else "Se despintan con facilidad",
                color = Color(0xFF5A3C1D)
            )
        }
    }
}
