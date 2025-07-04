package com.proyecto.ReUbica.ui.screens.PersonalInformationScreen

import android.app.Application
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.ui.Components.ListItemRow
import com.proyecto.ReUbica.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.user.UpdateProfileRequest
import com.proyecto.ReUbica.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI
import androidx.core.net.toUri

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

    val session by personalInformationViewModel.userSession.collectAsState()

    var editField by remember { mutableStateOf<String?>(null) }
    var editValue by remember { mutableStateOf("") }

    val imageUri = remember { mutableStateOf<Uri?>(null) }

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
                if (imageUri.value != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri.value),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Default Profile Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(120.dp)
                    )
                }

                IconButton(
                    onClick = {  },
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

        Column (
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
                        val user = session?.userProfile ?: return@Button

                        when (editField) {
                            "firstname", "lastname" -> {
                                if (editValue.isBlank() || !editValue.all { it.isLetter() || it.isWhitespace() }) {
                                    errorMessage = "Solo se permiten letras en tu nombre y/o apellido"
                                    return@Button
                                }
                            }
                            "email" -> {
                                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
                                if (!emailRegex.matches(editValue)) {
                                    errorMessage = "Correo inválido"
                                    return@Button
                                }
                            }
                            "phone" -> {
                                val phoneRegex = "^\\d{4}-\\d{4}$".toRegex()
                                if (!phoneRegex.matches(editValue)) {
                                    errorMessage = "Teléfono inválido. Formato esperado: XXXX-XXXX"
                                    return@Button
                                }
                            }
                        }

                        val updated = UpdateProfileRequest(
                            firstname = if (editField == "firstname") editValue else user.firstname ?: "",
                            lastname = if (editField == "lastname") editValue else user.lastname ?: "",
                            email = if (editField == "email") editValue else user.email ?: "",
                            phone = if (editField == "phone") editValue else user.phone ?: "",
                            user_icon = user.user_icon
                        )
                        personalInformationViewModel.updateProfile(updated)
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

}