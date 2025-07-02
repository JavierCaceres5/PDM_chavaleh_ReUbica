package com.proyecto.ReUbica.ui.screens.CartaProductos

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import com.proyecto.ReUbica.data.repository.ProductoRepository
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen3Navigation
import com.proyecto.ReUbica.utils.ViewModelFactory

@Composable
fun CartaProductosScreen(
    navController: NavHostController
) {

    val context = LocalContext.current
    val viewModel: CartaProductosViewModel = viewModel(
        factory = ViewModelFactory(context.applicationContext as Application) {
            CartaProductosViewModel(it)
        }
    )

    val productos by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.profile),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Text(
            text = "Tu carta de productos",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A3C1D),
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate(RegisterLocalScreen3Navigation) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar",
                    tint = Color(0xFF5A3C1D),
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(
                text = "Agregar producto",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 16.sp
            )
        }

        when {
            loading -> {
                Text("Cargando productos...", modifier = Modifier.padding(16.dp))
            }

            error != null -> {
                Text("Error: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(productos) { _, producto ->
                        HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.3f))

                        ProductoItem(producto)

                        HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: ProductoModel) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Text(
            text = producto.nombre.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A3C1D)
        )

        Text(
            text = producto.descripcion.toString(),
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Text(
            text = "Precio: $${producto.precio}",
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        AsyncImage(
            model = producto.product_image,
            contentDescription = "Imagen del producto",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )
    }
}
