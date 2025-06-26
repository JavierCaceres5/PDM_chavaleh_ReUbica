package com.proyecto.ReUbica.ui.screens.ComercioScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import com.proyecto.ReUbica.ui.screens.ComercioScreen.ComercioViewModel

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavHostController,
    viewModel: ComercioViewModel = viewModel()
) {
    // ️ Solo mientras no juntamos la base de datos o sea el back:
    LaunchedEffect(Unit) {
        viewModel.setBusinessInfo(
            navArgs = ComercioNavigation(
                id = "1",
                nombre = "Café Dani",
                descripcion = "Delicias salvadoreñas",
                categoria = "Cafetería",
                direccion = "UCA",
                latitud = 13.682,
                longitud = -89.240,
                horario = "8:00 AM - 5:00 PM"
            )
        )
    }

    val business by viewModel.business.collectAsState()
    val product = business.products.find { it.id.toString() == productId }

    if (product == null) {
        Text("Producto no encontrado", color = Color.Red)
        return
    }

    var comentario by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Imagen placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A3C1D)
        )
        Text(product.description, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Text("$${product.price}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Schedule, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Horario 2-5 pm")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Universidad José Simeón Cañas")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Email, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("correo@ejemplo.com")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Calificación: ", fontWeight = FontWeight.Bold)
            repeat(product.rating.toInt()) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700))
            }
            Text(" ${product.rating}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // Comentarios a la izquierda
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("💡 Comentarios de usuarios", fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(8.dp))

                listOf(
                    Triple("https://randomuser.me/api/portraits/women/44.jpg", "Fernanda", 4),
                    Triple("https://randomuser.me/api/portraits/men/45.jpg", "Jorge", 1)
                ).forEach { (imageUrl, nombre, rating) ->
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(30.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(nombre, fontWeight = FontWeight.Bold, color = Color(0xFF5A3C1D))
                                Spacer(Modifier.width(8.dp))
                                repeat(rating) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700))
                                }
                                repeat(5 - rating) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Gray)
                                }
                            }
                            Text(
                                if (nombre == "Fernanda") "Me pareció increíble" else "Se despintan con facilidad",
                                color = Color(0xFF5A3C1D)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Área para escribir comentario a la derecha
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Escribe tu comentario") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {  },
                    modifier = Modifier.align(Alignment.End),
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
