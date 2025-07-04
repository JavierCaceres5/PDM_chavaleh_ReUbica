package com.proyecto.ReUbica.ui.screens.PersonalInformationScreen

import android.app.Application
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.Components.ListItemRow
import com.proyecto.ReUbica.utils.ViewModelFactory

@Composable
fun PersonalInformationScreen(
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val personalInformationViewModel: PersonalInformationViewModel = viewModel(
        factory = ViewModelFactory(application) { app ->
            PersonalInformationViewModel(app)
        }
    )
    val success by personalInformationViewModel.success.collectAsState()
    val error by personalInformationViewModel.error.collectAsState()

    val session by personalInformationViewModel.userSession.collectAsState()

    var editField by remember { mutableStateOf<String?>(null) }
    var editValue by remember { mutableStateOf("") }

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val showConfirmImageDialog = remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri.value = it
            showConfirmImageDialog.value = true
        }
    }

    fun openEditDialog(field: String, currentValue: String) {
        editField = field
        editValue = currentValue
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

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 30.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                val imagePainter = rememberAsyncImagePainter(
                    model = imageUri.value ?: session?.userProfile?.user_icon,
                    placeholder = painterResource(R.drawable.profile), // Tu imagen default
                    error = painterResource(R.drawable.profile) // Lo mismo si hay error
                )


                Image(
                    painter = imagePainter,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = {
                        imagePickerLauncher.launch("image/*")
                    },
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

        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = "Nombre completo",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 15.dp)
            )
            ListItemRow(
                text = session?.userProfile?.firstname ?: "",
                onClick = {
                    openEditDialog("firstname", session?.userProfile?.firstname ?: "")
                },
                icon = Icons.Filled.Edit,
            )
            ListItemRow(
                text = session?.userProfile?.lastname ?: "",
                onClick = {
                    openEditDialog("lastname", session?.userProfile?.lastname ?: "")
                },
                icon = Icons.Filled.Edit
            )
            Text(
                text = "Correo Electrónico",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 15.dp)
            )
            ListItemRow(
                text = session?.userProfile?.email ?: "",
                onClick = {
                    openEditDialog("email", session?.userProfile?.email ?: "")
                },
                icon = Icons.Filled.Edit
            )

            Text(
                text = "Teléfono",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 15.dp)
            )
            ListItemRow(
                text = session?.userProfile?.phone ?: "",
                onClick = {
                    openEditDialog("phone", session?.userProfile?.phone ?: "")
                },
                icon = Icons.Filled.Edit
            )
        }
    }

    if (editField != null) {
        var errorMessage by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = {
                editField = null
                errorMessage = null
            },
            confirmButton = {
                Button(
                    onClick = {
                        personalInformationViewModel.updateProfile(
                            firstname = if (editField == "firstname") editValue else null,
                            lastname  = if (editField == "lastname")  editValue else null,
                            email     = if (editField == "email")     editValue else null,
                            phone     = if (editField == "phone")     editValue else null,
                            uri = null,
                            context = context
                        )
                        editField = null
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A3C1D), contentColor = Color.White)
                ) {
                    Text("Guardar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        editField = null
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
                ) {
                    Text("Cancelar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            },
            title = {
                Text(
                    text = "Editar ${when (editField) {
                        "firstname" -> "Nombre"
                        "lastname" -> "Apellido"
                        "email" -> "Correo"
                        "phone" -> "Teléfono"
                        else -> "Campo"
                    }}",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {

                    OutlinedTextField(
                        value = editValue,
                        onValueChange = {
                            editValue = when (editField) {
                                "firstname", "lastname" -> it.filter { c -> c.isLetter() || c.isWhitespace() }
                                "phone" -> it.filter { c -> c.isDigit() || c == '-' }
                                else -> it
                            }
                            errorMessage = null
                        },
                        label = { Text("Nuevo valor") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5A3C1D),
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color(0xFF5A3C1D)
                        ),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        )
    }

    if (showConfirmImageDialog.value && imageUri.value != null) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showConfirmImageDialog.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        personalInformationViewModel.updateProfile(
                            firstname = null,
                            lastname = null,
                            email = null,
                            phone = null,
                            uri = imageUri.value,
                            context = context
                        )
                        showConfirmImageDialog.value = false
                        imageUri.value = null
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A3C1D), contentColor = Color.White)
                ) {
                    Text("Guardar foto", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showConfirmImageDialog.value = false
                        imageUri.value = null
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
                ) {
                    Text("Cancelar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            },
            title = {
                Text(
                    text = "Actualizar foto de perfil",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "¿Estás seguro que deseas actualizar tu foto de perfil?",
                    color = Color.Black
                )
            }
        )
    }

}

