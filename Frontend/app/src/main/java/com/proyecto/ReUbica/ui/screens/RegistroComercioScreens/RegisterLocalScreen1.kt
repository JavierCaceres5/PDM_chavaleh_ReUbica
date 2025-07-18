package com.proyecto.ReUbica.ui.screens.RegistroComercioScreens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.layouts.StepTopBar
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen2Navigation
import androidx.compose.ui.platform.LocalContext
import com.proyecto.ReUbica.utils.getRealPathFromUri

@Composable
fun RegisterLocalScreen1(navController: NavHostController, viewModelComercio: RegistroComercioViewModel, viewModelProducto: CreateProductoViewModel) {

    RegisterLocalScreen1Content(
        registroComercio = viewModelComercio,
        createProducto = viewModelProducto,
        onNext = { navController.navigate(RegisterLocalScreen2Navigation) },
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun RegisterLocalScreen1Content(
    createProducto: CreateProductoViewModel,
    registroComercio: RegistroComercioViewModel,
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val poppins = FontFamily(Font(R.font.poppinsextrabold))
    val abel = FontFamily(Font(R.font.abelregular))

    val categoriasPrincipales = listOf(
        "Comida",
        "Alimentos",
        "Higiene",
        "Servicios",
        "Ropa",
        "Libreria",
        "Artesanias"
    )
    val categoriasSecundarias = mapOf(
        "Comida" to listOf(
            "Frutas", "Verduras", "Lácteos", "Productos enlatados", "Snacks",
            "Dulces típicos", "Carnes", "Pescados y mariscos",
            "Comida saludable", "Comida mexicana", "Comida asiática",
            "Hot Dogs", "Hamburguesas", "Pizzas", "Otros"
        ),
        "Alimentos" to listOf(
            "Frutas", "Verduras", "Lácteos", "Productos enlatados", "Snacks",
            "Dulces típicos", "Carnes", "Pescados y mariscos"
        ),
        "Higiene" to listOf(
            "Limpieza"
        ),
        "Servicios" to listOf(
            "Reparación electrónica", "Consultoría", "Transporte"
        ),
        "Ropa" to listOf(
            "Ropa de segunda", "Vestidos", "Accesorios", "Calzados", "Ropa variada"
        ),
        "Libreria" to listOf(
            "Libros infantiles", "Novelas", "Papelería", "Material escolar", "Revistas"
        ),
        "Artesanias" to listOf(
            "Cerámica", "Tejidos", "Joyería artesanal", "Cuadros"
        )
    )


    val emprendimiento by registroComercio.emprendimiento.collectAsState()


    var expandedTipo by remember { mutableStateOf(false) }
    var expandedCategoria by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var descripcionInvalida by remember { mutableStateOf(false) }


    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val logoPath = emprendimiento.logo

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            val realPath = getRealPathFromUri(context, uri)
            registroComercio.setValues("logo", realPath)
        }
    }


    val scrollState = rememberScrollState()
    val subcategorias = categoriasSecundarias[emprendimiento.categoriasPrincipales.firstOrNull()] ?: emptyList()

    Column(modifier = Modifier.fillMaxSize()) {
        StepTopBar(step = 1, title = "Datos del negocio", onBackClick = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "¡Comencemos tu proceso de registro!",
                fontFamily = poppins,
                fontSize = 24.sp,
                color = Color(0xFF5A3C1D),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Esta información nos sirve para empezar el registro de tu local.",
                fontFamily = abel,
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(24.dp))

            Text(label = "Nombre del local", value = emprendimiento.nombre, onChange = { registroComercio.setValues( "nombre", it ) })
            Text(label = "Descripción del negocio", value = emprendimiento.descripcion, onChange = { registroComercio.setValues( "descripcion", it)})

            if (descripcionInvalida) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .background(Color(0xFFFFE6E6), shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFD32F2F), shape = RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Error de descripción",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "La descripción no puede superar los 500 caracteres.",
                        color = Color(0xFFD32F2F),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.abelregular)),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Dropdown(
                label = "Tipo de Negocio",
                value = emprendimiento.categoriasPrincipales.firstOrNull() ?: "",
                options = categoriasPrincipales,
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = it },
                onSelect = { seleccion ->
                    registroComercio.setCategoriasPrincipales(listOf(seleccion))
                    registroComercio.setCategoriasSecundarias(emptyList())
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Dropdown(
                label = "Categoría",
                value = emprendimiento.categoriasSecundarias.firstOrNull() ?: "",
                options = subcategorias,
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = it },
                onSelect = { seleccion ->
                    registroComercio.setCategoriasSecundarias(listOf(seleccion))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Adjunte el logo de su comercio",
                fontFamily = poppins,
                fontSize = 16.sp,
                color = Color(0xFF5A3C1D)
            )

            Text(
                "Asegúrese que el logo adjunto se vea correctamente. Solo archivos con formatos JPEG, PDF o PNG podrán ser convertidos",
                fontFamily = abel,
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (!logoPath.isNullOrBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(logoPath),
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
                        Text("Seleccione una imagen", fontFamily = abel, fontSize = 12.sp, color = Color.Black)
                    }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            if (showError) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                            text = "Por favor, complete todos los campos antes de continuar",
                            color = Color(0xFFD32F2F),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.abelregular)),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.weight(1f)

                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Button(
                onClick = {

                    val descripcionValida = emprendimiento.descripcion.length <= 500

                    if (
                        emprendimiento.nombre.isBlank() ||
                        emprendimiento.descripcion.isBlank() ||
                        emprendimiento.categoriasPrincipales.firstOrNull().isNullOrBlank() ||
                        emprendimiento.categoriasSecundarias.firstOrNull().isNullOrBlank()
                    ) {
                        showError = true
                    } else if (!descripcionValida) {
                        showError = false
                        descripcionInvalida = true
                    } else {
                        showError = false
                        descripcionInvalida = false
                        onNext()
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF49724C),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Continuar")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun Text(label: String, value: String, onChange: (String) -> Unit) {
    val poppins = FontFamily(Font(R.font.poppinsextrabold))
    val abel = FontFamily(Font(R.font.abelregular))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = label,
            fontFamily = poppins,
            fontSize = 14.sp,
            color = Color(0xFF5A3C1D),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = {
                Text(
                    text = label,
                    fontFamily = abel,
                    color = Color.Black
                )
            },
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFDFF2E1), RoundedCornerShape(8.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color(0xFFDFF2E1),
                unfocusedContainerColor = Color(0xFFDFF2E1)
            )
        )
    }
}

@Composable
fun Dropdown(
    label: String,
    value: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelect: (String) -> Unit
) {
    val poppins = FontFamily(Font(R.font.poppinsextrabold))
    val abel = FontFamily(Font(R.font.abelregular))
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            if (expanded && options.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                        .background(Color.White, shape = RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFF49724C), shape = RoundedCornerShape(10.dp))
                        .padding(start = 4.dp, end = 4.dp, top = 6.dp, bottom = 8.dp)
                ) {
                    options.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFDFF2E1))
                                .clickable {
                                    onSelect(item)
                                    onExpandedChange(false)
                                }
                                .padding(vertical = 10.dp, horizontal = 14.dp)
                        ) {
                            Text(
                                text = item.replaceFirstChar(Char::uppercase),
                                fontFamily = abel,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }

                        if (index != options.lastIndex) {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }

            Text(
                text = label,
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
            )

            OutlinedTextField(
                value = value.ifBlank { "--Ninguno--" }.replaceFirstChar { it.uppercase() },
                onValueChange = {},
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                trailingIcon = {
                    IconButton(onClick = { onExpandedChange(!expanded) }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFDFF2E1), RoundedCornerShape(8.dp))
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
    }
}
