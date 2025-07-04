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
import com.proyecto.ReUbica.data.model.producto.ProductoModel
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = product.nombre.orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.descripcion.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5A3C1D),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${product.precio}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF5A3C1D)
                )
            }

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
                        text = String.format("%.1f", promedioRating),
                        fontSize = 20.sp,
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
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        if (hasHalfStar) {
                            Icon(
                                imageVector = Icons.Default.StarHalf,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        repeat(5 - fullStars - if (hasHalfStar) 1 else 0) {
                            Icon(
                                imageVector = Icons.Default.StarOutline,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color(0xFF5A3C1D), thickness = 2.dp)
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "💡 Comentarios de usuarios",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF5D4F30)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (reviewLoading) {
                item {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF5A3C1D))
                    }
                }
            } else if (filteredReviews.isEmpty()) {
                item {
                    Text(
                        "Aún no hay reseñas de este producto.",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } else {
                items(filteredReviews) { review ->
                    ReviewItem(
                        review = review,
                        token = token,
                        reviewUserViewModel = reviewUserViewModel
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Deja tu reseña",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF5A3C1D)
                        )

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
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF5A3C1D),
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = Color(0xFF5A3C1D),
                                focusedLabelColor = Color(0xFF5A3C1D),
                                unfocusedLabelColor = Color(0xFF5A3C1D)
                            )
                        )

                        Button(
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
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A3C1D)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Enviar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewItem(
    review: ReviewModel,
    token: String,
    reviewUserViewModel: ReviewUserViewModel
) {
    val userProfiles by reviewUserViewModel.userProfiles.collectAsState()

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


