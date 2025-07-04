package com.proyecto.ReUbica.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.layouts.StepTopBar
import androidx.navigation.NavHostController
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Error
import com.proyecto.ReUbica.ui.navigations.HomeScreenNavigation
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.CreateProductoViewModel
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegistroComercioViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterLocalScreen3(
    navController: NavHostController,
    registroComercioViewModel: RegistroComercioViewModel,
    createProductoViewModel: CreateProductoViewModel,
    isAddingMoreProducts: Boolean = false
) {
    RegisterLocalScreen3Content(
        createProducto = createProductoViewModel,
        registroComercio = registroComercioViewModel,
        navController = navController,
        isAddingMoreProducts = isAddingMoreProducts,
        onNext = { navController.navigate(HomeScreenNavigation) },
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun RegisterLocalScreen3Content(
    createProducto: CreateProductoViewModel,
    registroComercio: RegistroComercioViewModel,
    navController: NavHostController,
    isAddingMoreProducts: Boolean = false,
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }

    val abel = FontFamily(Font(R.font.abelregular))
    val poppins = FontFamily(Font(R.font.poppinsextrabold))

    val context = LocalContext.current
    val producto by createProducto.producto.collectAsState()
    val imagenUri by createProducto.imagenUri.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var descripcionInvalida by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isSuccess by createProducto.success.collectAsState()
    var productosCargados by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val productosExistentes = registroComercio.obtenerProductosDelEmprendimiento()
        createProducto.setProductosExistentes(productosExistentes)
        productosCargados = true
        println("Productos cargados después de obtenerlos: $productosCargados")
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            createProducto.setImage(uri)
            createProducto.setValues("product_image", uri.toString())
        }
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            errorMessage = null
            showDialog = true
            createProducto.clearProducto()
            createProducto.setImage(null)
            createProducto.resetSuccess()
        }
    }

    Column(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!isAddingMoreProducts) {

                StepTopBar(step = 2, title = "Carta de productos", onBackClick = onBack)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡Comencemos con tus productos!",
                    fontFamily = poppins,
                    fontSize = 24.sp,
                    color = Color(0xFF5A3C1D),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Esta información será visible en tu perfil y permitirá a los clientes conocer lo que ofreces en tu negocio.",
                    fontFamily = abel,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            } else {

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Agrega otro producto para tu carta",
                    fontFamily = poppins,
                    fontSize = 24.sp,
                    color = Color(0xFF5A3C1D),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            errorMessage?.let {
                ErrorMessageBox(message = it, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Nombre del producto",
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.fillMaxWidth()
            )
            ProductInputField(
                placeholder = "Nombre del producto",
                value = producto.nombre
            ) {
                createProducto.setValues("nombre", it)
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Descripción del producto",
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.fillMaxWidth()
            )
            ProductInputField(
                placeholder = "Descripción del producto",
                value = producto.descripcion
            ) {
                createProducto.setValues("descripcion", it)
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Precio",
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.fillMaxWidth()
            )
            ProductInputField(
                placeholder = "Precio del producto",
                value = if (producto.precio == 0.0) "" else producto.precio.toString(),
                isPrice = true
            ) {
                createProducto.setValues("precio", it)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Adjunte foto del producto",
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Asegúrese de que el menú adjunto se vea correctamente. Sólo archivos con formatos JPEG, PDF o PNG podrán ser convertidos",
                fontFamily = abel,
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                    .clickable { launcher.launch("image/*") }
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                if (imagenUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imagenUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Upload,
                            contentDescription = "Subir",
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            "Seleccione una imagen",
                            fontFamily = abel,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (showError) {
                ErrorMessageBox(
                    message = "Por favor, complete todos los campos antes de continuar",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (descripcionInvalida) {
                ErrorMessageBox(
                    message = "La descripción no puede exceder los 150 caracteres.",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        errorMessage = null
                        showError = false
                        descripcionInvalida = false

                        val nombreValido = producto.nombre.matches(Regex("^[A-Za-záéíóúÁÉÍÓÚñÑ ]+\$"))
                        val precioValido = producto.precio > 0.0

                        if (producto.nombre.isBlank() || producto.descripcion.isBlank() || !precioValido || imagenUri == null) {
                            errorMessage = "Por favor, complete todos los campos antes de continuar."
                            println("Error: $errorMessage")
                        } else if (!nombreValido) {
                            errorMessage = "El nombre del producto solo puede contener letras y espacios."
                            println("Error: $errorMessage")
                        } else if (!productosCargados) {
                            errorMessage = "Espere a que se carguen los productos."
                            println("Error: $errorMessage")
                        } else if (createProducto.nombreProductoExiste(producto.nombre)) {
                            errorMessage = "Ya existe un producto con este nombre en tu emprendimiento."
                            println("Error: $errorMessage")
                        } else {
                            errorMessage = null
                            coroutineScope.launch {
                                createProducto.crearProducto(context)
                            }
                        }
                    },
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF49724C)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Agregar", color = Color.White, fontFamily = abel)
                }

                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Continuar", color = Color.White, fontFamily = abel)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "¡Todo listo!",
                    fontFamily = poppins,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    "Tu producto ha sido publicado con éxito.",
                    fontFamily = abel,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                OutlinedButton(
                    onClick = { showDialog = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Continuar", fontFamily = poppins)
                }
            },
            containerColor = Color.White,
            tonalElevation = 6.dp,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun ProductInputField(
    placeholder: String,
    value: String,
    isPrice: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val abel = FontFamily(Font(R.font.abelregular))

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (isPrice) {
                val cleanValue = it.filter { char -> char.isDigit() || char == '.' }
                onValueChange(cleanValue)
            } else {
                onValueChange(it)
            }
        },
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = abel,
                color = if (isPrice) Color(0xFF8AA09A) else Color.Black
            )
        },
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontFamily = abel
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDFF2E1), RoundedCornerShape(8.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color(0xFFDFF2E1),
            unfocusedContainerColor = Color(0xFFDFF2E1),
            cursorColor = Color.Black
        ),
        leadingIcon = if (isPrice) {
            {
                Text(
                    text = "$",
                    fontFamily = abel,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        } else null,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = if (isPrice) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
    )
}

@Composable
fun ErrorMessageBox(message: String, modifier: Modifier = Modifier) {
    val abel = FontFamily(Font(R.font.abelregular))

    Row(
        modifier = modifier
            .background(Color(0xFFFFE6E6), shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFD32F2F), shape = RoundedCornerShape(8.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Error,
            contentDescription = "Error",
            tint = Color(0xFFD32F2F),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message,
            color = Color(0xFFD32F2F),
            fontSize = 14.sp,
            fontFamily = abel,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
    }
}
