package com.proyecto.ReUbica.ui.screens.CartaProductos

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

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

    val listState = rememberLazyListState()

    val productos by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val eliminando by viewModel.eliminando.collectAsState()
    var confirmEliminando by remember { mutableStateOf<ProductoModel?>(null) }
    var showDialogDelete by remember { mutableStateOf(false) }

    var productoParaEditar by remember { mutableStateOf<ProductoModel?>(null) }
    var showDialogEditar by remember { mutableStateOf(false) }

    var nombreEditado by remember { mutableStateOf("") }
    var descripcionEditada by remember { mutableStateOf("") }
    var precioEditado by remember { mutableStateOf("") }
    var imagenEditada by remember { mutableStateOf("") }
    var nuevaImagenUri by remember { mutableStateOf<Uri?>(null) }

// Picker:
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { nuevaImagenUri = it }
    }
    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        if (eliminando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF49724C))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(215.dp)
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

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .padding(30.dp),
                    state = listState
                ) {
                    itemsIndexed(productos) { index, producto ->

                        if (index == 0) {
                            HorizontalDivider(thickness = 3.dp, color = Color(0xFF49724C).copy(alpha = 1f))
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        ProductoItem(
                            producto = producto,
                            onEditarClick = { productoId ->
                                productoParaEditar = producto
                                nombreEditado = producto.nombre ?: ""
                                descripcionEditada = producto.descripcion ?: ""
                                precioEditado = producto.precio?.toString() ?: ""
                                imagenEditada = producto.product_image ?: ""
                                showDialogEditar = true
                            },
                            onEliminarClick = { productoId ->
                                confirmEliminando = producto
                                showDialogDelete = true
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(thickness = 3.dp, color = Color(0xFF49724C).copy(alpha = 2f))
                    }
                }
            }
        }
    }
    if (showDialogDelete && confirmEliminando != null) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialogDelete = false },
            title = { Text("Confirmar eliminación", color = Color.Black, textAlign = TextAlign.Justify) },
            text = { Text("¿Seguro que quieres eliminar el producto \"${confirmEliminando!!.nombre}\"?"
                , color = Color.Black, textAlign = TextAlign.Justify ) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialogDelete = false
                        confirmEliminando?.let {
                            viewModel.eliminarProducto(it.id.toString())  // <-- AQUÍ
                        }
                        confirmEliminando = null
                    },
                    modifier = Modifier.width(130.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8E210B),
                        contentColor = Color.White
                    )
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialogDelete = false
                        confirmEliminando = null
                    },
                    modifier = Modifier.width(130.dp),
                    shape = RoundedCornerShape(0.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDialogEditar && productoParaEditar != null) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = {
                showDialogEditar = false
                productoParaEditar = null
                nuevaImagenUri = null
            },
            title = {
                Text(
                    text = "Editar producto",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = nombreEditado,
                        onValueChange = { nombreEditado = it },
                        label = { Text("Nombre", color = Color.Black) },
                        textStyle = TextStyle(color = Color.Black),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = descripcionEditada,
                        onValueChange = { descripcionEditada = it },
                        label = { Text("Descripción", color = Color.Black) },
                        textStyle = TextStyle(color = Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = precioEditado,
                        onValueChange = { precioEditado = it },
                        label = { Text("Precio", color = Color.Black) },
                        textStyle = TextStyle(color = Color.Black),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Imagen actual o nueva
                    if (nuevaImagenUri != null) {
                        AsyncImage(
                            model = nuevaImagenUri,
                            contentDescription = "Nueva imagen del producto",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        )
                    } else if (imagenEditada.isNotBlank()) {
                        AsyncImage(
                            model = imagenEditada,
                            contentDescription = "Imagen actual del producto",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        )
                    }

                    TextButton(
                        onClick = { pickImageLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cambiar imagen", color = Color(0xFF49724C))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialogEditar = false
                        productoParaEditar?.let {
                            viewModel.actualizarProducto(
                                productoId = it.id.toString(),
                                nombre = nombreEditado,
                                descripcion = descripcionEditada,
                                precio = precioEditado.toDoubleOrNull() ?: 0.0,
                                nuevaImagenUri = nuevaImagenUri
                            )
                        }
                        productoParaEditar = null
                        nuevaImagenUri = null // <- Limpia selección al guardar
                    },
                    modifier = Modifier
                        .width(130.dp)
                        .background(Color(0xFF49724C), shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        "Guardar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialogEditar = false
                        productoParaEditar = null
                        nuevaImagenUri = null // <- Limpia selección al cancelar
                    },
                    modifier = Modifier
                        .width(130.dp)
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                ) {
                    Text("Cancelar", color = Color.Black)
                }
            }
        )
    }



}

@Composable
fun ProductoItem(
    producto: ProductoModel,
    onEliminarClick: (String) -> Unit,
    onEditarClick: (String) -> Unit
) {

    Row {
        AsyncImage(
            model = producto.product_image,
            contentDescription = "Imagen del producto",
            modifier = Modifier
                .width(200.dp)
                .height(125.dp)
                .padding(end = 18.dp),
            contentScale = ContentScale.Fit
        )

        Column (
            modifier = Modifier.weight(1f)
        )  {
            Text(
                text = producto.nombre.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = producto.descripcion.toString(),
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Precio: $${producto.precio}",
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row  {
                IconButton(
                    onClick = { onEditarClick(producto.id.toString()) },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(1.dp)
                ){
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar",
                        tint = Color.Black,
                        modifier = Modifier.padding(1.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = { onEliminarClick(producto.id.toString()) },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(1.dp)
                ){
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Black,
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }
        }
    }
}
