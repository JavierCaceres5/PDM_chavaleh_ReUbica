package com.proyecto.ReUbica.ui.screens.ProductoScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import com.proyecto.ReUbica.ui.screens.ComercioScreen.ComercioViewModel

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavHostController,
    viewModel: ComercioViewModel = viewModel()
) {
    val business by viewModel.business.collectAsState()
    val product = business.products.find { it.id.toString() == productId }
    var ratingSeleccionado by remember { mutableStateOf(0) }

    if (product == null) {
        Text("Producto no encontrado", color = Color.Red)
        return
    }

    var comentario by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
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

        Text("$${product.precio}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF5A3C1D))

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Column (modifier = Modifier.fillMaxWidth(0.6f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.SettingsPhone, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(text = "8874-7387" , color = Color(0xFF5A3C1D))
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Schedule, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(text = "Horario 2-5 pm" , color = Color(0xFF5A3C1D))
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(text = "Universidad José Simeón Cañas", color = Color(0xFF5A3C1D))
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Email, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(text = "correo@ejemplo.com", color = Color(0xFF5A3C1D))
                }
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
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "Ya casi",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4F30) // color café como en la imagen
                        )
                        //Row {
                        //    repeat(5) { index ->
                        //        Icon(
                        //            imageVector = if (index < product.rating.toInt()) Icons.Default.Star else Icons.Default.StarOutline,
                        //            contentDescription = null,
                        //            tint = Color(0xFFFFD700),
                        //            modifier = Modifier.size(16.dp)
                        //        )
                        //    }
                        //}
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Divider(color = Color(0xFF5A3C1D), thickness = 2.dp)

        Spacer(modifier = Modifier.height(8.dp))

        Text("💡 Comentarios de usuarios", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D4F30))

            // Comentarios a la izquierda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp)
        ) {
            // Comentarios scrollables
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
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(15.dp))
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

            // Área para escribir comentario a la derecha
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < ratingSeleccionado) Icons.Default.Star else Icons.Default.StarOutline,
                            contentDescription = "Estrella ${index + 1}",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { ratingSeleccionado = index + 1 }
                        )

                    }
                }

                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Escribe tu comentario") },
                    textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                    modifier = Modifier.fillMaxWidth()
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.End)
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
