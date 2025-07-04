package com.proyecto.ReUbica.ui.screens.PersonalInformationScreen

import android.app.Application
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.Components.FormatearURL
import com.proyecto.ReUbica.ui.screens.LocalInformationScreen.EmprendimientoViewModel
import com.proyecto.ReUbica.utils.ViewModelFactory
import androidx.core.net.toUri
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.proyecto.ReUbica.data.model.emprendimiento.RedesSociales
import com.proyecto.ReUbica.data.model.emprendimiento.UpdateEmprendimientoRequest

@Composable
fun LocalInformationScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val viewModel: EmprendimientoViewModel = viewModel(
        factory = ViewModelFactory(application) { EmprendimientoViewModel(application) }
    )

    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            val realPath = com.proyecto.ReUbica.utils.getRealPathFromUri(context, uri)
            viewModel.updateEmprendimientoLogo(realPath)
        }
    }

    val scrollState = rememberScrollState()
    var showRedes by remember { mutableStateOf(false) }
    val emprendimiento by viewModel.emprendimiento.collectAsState()
    val redesSociales by viewModel.redesSociales.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val logoPath = emprendimiento?.logo


    var editField by remember { mutableStateOf<String?>(null) }
    var editValue by remember { mutableStateOf(TextFieldValue("")) }
    var errorUrl by remember { mutableStateOf(false) }
    var errorCampoVacio by remember { mutableStateOf(false) }
    var errorFormatoTelefono by remember { mutableStateOf(false) }

    fun openEditDialog(field: String, currentValue: String) {
        editField = field
        editValue = TextFieldValue(currentValue)
        errorUrl = false
        errorCampoVacio = false
        errorFormatoTelefono = false
    }

    fun esUrlValida(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    LaunchedEffect(Unit) {
        viewModel.cargarMiEmprendimiento()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Emprendimiento Background Image",
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

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 30.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                when {
                    selectedImageUri != null -> {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Nueva Imagen",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    !logoPath.isNullOrBlank() -> {
                        Image(
                            painter = rememberAsyncImagePainter(logoPath),
                            contentDescription = "Emprendimiento Image",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Filled.Store,
                            contentDescription = "Default Emprendimiento Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }

                IconButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-20).dp, y = (-36).dp)
                        .size(28.dp)
                        .background(Color(0xFF5A3C1D), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Photo",
                        tint = Color.White
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(35.dp))

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (error != null) {
            Text(
                text = error ?: "",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        } else if (emprendimiento != null) {

            Column(modifier = Modifier.padding(25.dp)) {

                EditableTextSection(
                    title = "Nombre del local",
                    text = emprendimiento?.nombre ?: "",
                    onEditClick = { openEditDialog("nombre", emprendimiento?.nombre ?: "") }
                )
                Spacer(modifier = Modifier.height(12.dp))

                EditableTextSection(
                    title = "Descripción del local",
                    text = emprendimiento?.descripcion ?: "",
                    onEditClick = {
                        openEditDialog(
                            "descripcion",
                            emprendimiento?.descripcion ?: ""
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))

                EditableTextSection(
                    title = "Tipo de negocio",
                    text = emprendimiento?.categoriasPrincipales?.joinToString(", ") ?: "",
                    editable = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                EditableTextSection(
                    title = "Categoría del local",
                    text = emprendimiento?.categoriasSecundarias?.joinToString(", ") ?: "",
                    editable = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                EditableTextSection(
                    title = "Dirección del local",
                    text = emprendimiento?.direccion ?: "",
                    onEditClick = { openEditDialog("direccion", emprendimiento?.direccion ?: "") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                EditableTextSection(
                    title = "Teléfono del local",
                    text = emprendimiento?.emprendimientoPhone ?: "",
                    onEditClick = {
                        openEditDialog(
                            "emprendimientoPhone",
                            emprendimiento?.emprendimientoPhone ?: ""
                        )
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Redes sociales",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5A3C1D),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 15.dp, bottom = 10.dp)
                    )
                    TextButton(onClick = { showRedes = !showRedes }) {
                        Text(
                            if (showRedes) "Ocultar" else "Ver más",
                            color = Color(0xFF013F6D),
                            modifier = Modifier.padding(end = 5.dp, top = 8.dp)
                        )
                    }
                }

                if (showRedes) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        FormatearURL(
                            url = redesSociales?.Instagram,
                            nombreRed = "Instagram",
                            iconId = R.drawable.instagram,
                            onEditarClick = {
                                openEditDialog(
                                    "instagram",
                                    redesSociales?.Instagram ?: ""
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FormatearURL(
                            url = redesSociales?.Facebook,
                            nombreRed = "Facebook",
                            iconId = R.drawable.facebook,
                            onEditarClick = {
                                openEditDialog(
                                    "facebook",
                                    redesSociales?.Facebook ?: ""
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FormatearURL(
                            url = redesSociales?.TikTok,
                            nombreRed = "TikTok",
                            iconId = R.drawable.tiktok,
                            onEditarClick = {
                                openEditDialog(
                                    "tiktok",
                                    redesSociales?.TikTok ?: ""
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FormatearURL(
                            url = redesSociales?.Twitter,
                            nombreRed = "X",
                            iconId = R.drawable.x,
                            onEditarClick = {
                                openEditDialog(
                                    "twitter",
                                    redesSociales?.Twitter ?: ""
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (editField != null) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = {
                editField = null
                errorUrl = false
                errorCampoVacio = false
                errorFormatoTelefono = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        val current = emprendimiento
                        if (current != null) {
                            if (editValue.text.isBlank()) {
                                errorCampoVacio = true
                                return@Button
                            }

                            if (editField in listOf(
                                    "instagram",
                                    "facebook",
                                    "tiktok",
                                    "twitter"
                                ) && !esUrlValida(editValue.text)
                            ) {
                                errorUrl = true
                                return@Button
                            }

                            if (editField == "emprendimientoPhone") {
                                val phoneRegex = "^\\d{4}-\\d{4}$".toRegex()
                                if (!phoneRegex.matches(editValue.text)) {
                                    errorFormatoTelefono = true
                                    return@Button
                                }
                            }

                            val nuevasRedes = current.redes_sociales?.copy(
                                Instagram = if (editField == "instagram") editValue.text else current.redes_sociales.Instagram,
                                Facebook = if (editField == "facebook") editValue.text else current.redes_sociales.Facebook,
                                TikTok = if (editField == "tiktok") editValue.text else current.redes_sociales.TikTok,
                                Twitter = if (editField == "twitter") editValue.text else current.redes_sociales.Twitter
                            ) ?: RedesSociales()

                            val updated = UpdateEmprendimientoRequest(
                                nombre = if (editField == "nombre") editValue.text else current.nombre
                                    ?: "",
                                descripcion = if (editField == "descripcion") editValue.text else current.descripcion
                                    ?: "",
                                categoriasPrincipales = current.categoriasPrincipales,
                                categoriasSecundarias = current.categoriasSecundarias,
                                direccion = if (editField == "direccion") editValue.text else current.direccion
                                    ?: "",
                                emprendimientoPhone = if (editField == "emprendimientoPhone") editValue.text else current.emprendimientoPhone
                                    ?: "",
                                redes_sociales = nuevasRedes
                            )

                            viewModel.updateEmprendimiento(updated)
                            editField = null
                            errorUrl = false
                            errorCampoVacio = false
                            errorFormatoTelefono = false
                        }
                    },
                    modifier = Modifier.width(130.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        editField = null
                        errorUrl = false
                        errorCampoVacio = false
                        errorFormatoTelefono = false
                    },
                    modifier = Modifier.width(130.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8E210B),
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancelar")
                }
            },
            title = { Text("Editar $editField", color = Color.Black) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editValue,
                        onValueChange = {
                            if (editField == "emprendimientoPhone") {
                                val digits = it.text.filter { c -> c.isDigit() }.take(8)
                                val formatted = if (digits.length > 4) {
                                    digits.substring(0, 4) + "-" + digits.substring(4)
                                } else {
                                    digits
                                }

                                val newCursor = if (formatted.length > editValue.text.length) {
                                    formatted.length
                                } else {
                                    it.selection.start
                                }

                                editValue = TextFieldValue(
                                    text = formatted,
                                    selection = TextRange(newCursor.coerceAtMost(formatted.length))
                                )
                            } else {
                                editValue = it
                            }

                            errorUrl = false
                            errorCampoVacio = false
                            errorFormatoTelefono = false
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        keyboardOptions = if (editField == "emprendimientoPhone")
                            KeyboardOptions(keyboardType = KeyboardType.Number)
                        else KeyboardOptions.Default
                    )

                    if (errorCampoVacio) {
                        Text(
                            text = "El campo no puede estar vacío",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    if (errorUrl) {
                        Text(
                            text = "Ingrese una URL válida (debe iniciar con http:// o https://)",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    if (errorFormatoTelefono) {
                        Text(
                            text = "El formato debe ser XXXX-XXXX",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun EditableTextSection(
    title: String,
    text: String,
    editable: Boolean = true,
    onEditClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A3C1D),
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFDFF2E1), shape = RoundedCornerShape(8.dp))
                .padding(start = 10.dp, top = 6.dp, end = 10.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (editable) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFF5A3C1D)
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(40.dp))
            }
        }
    }
}