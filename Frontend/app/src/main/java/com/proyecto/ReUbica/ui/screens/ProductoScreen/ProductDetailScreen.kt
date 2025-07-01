package com.proyecto.ReUbica.ui.screens.ProductoScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
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
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavHostController,
    token: String,
    emprendimientoID: String,
    viewModel: ProductoViewModel = viewModel(),
    reviewViewModel: ReviewViewModel = viewModel(),
    reviewUserViewModel: ReviewUserViewModel = viewModel()
) {
    val productos by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val emprendimientoReviews by reviewViewModel.emprendimientoReviews.collectAsState()
    val reviewLoading by reviewViewModel.loading.collectAsState()
    val reviewError by reviewViewModel.error.collectAsState()

    var ratingSeleccionado by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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

    val promedioRating = if (filteredReviews.isNotEmpty()) {
        filteredReviews.map { it.rating }.average()
    } else {
        0.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

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
            product.nombre,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A3C1D)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            product.descripcion,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF5A3C1D)
        )

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
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFF5A3C1D), CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.1f", promedioRating),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4F30)
                        )
                        Row(horizontalArrangement = Arrangement.Center) {
                            val fullStars = floor(promedioRating).toInt()
                            val hasHalfStar = (promedioRating - fullStars) >= 0.5

                            repeat(fullStars) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            if (hasHalfStar) {
                                Icon(
                                    imageVector = Icons.Default.StarHalf, // Usa tu ícono de media estrella aquí si tienes uno
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            repeat(5 - fullStars - if (hasHalfStar) 1 else 0) {
                                Icon(
                                    imageVector = Icons.Default.StarOutline,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Divider(color = Color(0xFF5A3C1D), thickness = 2.dp)
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "💡 Comentarios de usuarios",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF5D4F30)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
            ) {
                if (reviewLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF5A3C1D))
                    }
                } else if (filteredReviews.isEmpty()) {
                    Text(
                        "Aún no hay reseñas de este producto.",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredReviews) { review ->
                            ReviewItem(
                                review = review,
                                token = token,
                                reviewUserViewModel = reviewUserViewModel
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
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
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .height(60.dp),
                )

                OutlinedButton(
                    onClick = {
                        if (comentario.isNotBlank() && ratingSeleccionado > 0) {
                            reviewViewModel.postReview(
                                token = token,
                                productoID = productId,
                                comentario = comentario,
                                rating = ratingSeleccionado.toDouble(),
                                emprendimientoID = emprendimientoID,
                                onConflict = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Ya has calificado este producto.")
                                    }
                                }
                            )
                            comentario = ""
                            ratingSeleccionado = 0
                        }
                    },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Enviar", color = Color(0xFF5A3C1D), fontWeight = FontWeight.Bold)
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
fun ReviewItem(
    review: ReviewModel,
    token: String,
    reviewUserViewModel: ReviewUserViewModel
) {
    val userProfiles by reviewUserViewModel.userProfiles.collectAsState()

    // Trigger fetch on load
    LaunchedEffect(review.userID) {
        reviewUserViewModel.getUserById(token, review.userID.toString())
    }

    val userProfile = userProfiles[review.userID.toString()]
    val userName = if (userProfile != null) {
        "${userProfile.firstname} ${userProfile.lastname}"
    } else {
        "Usuario"
    }

    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Column {
            Text(userName, fontWeight = FontWeight.Bold, color = Color(0xFF5A3C1D))
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


