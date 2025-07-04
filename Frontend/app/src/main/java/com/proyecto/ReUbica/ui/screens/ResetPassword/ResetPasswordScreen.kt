package com.proyecto.ReUbica.ui.screens.ResetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.navigations.LoginScreenNavigation

@Composable
fun ResetPasswordScreen(
    navController: NavHostController,
    resetPasswordViewModel: ResetPasswordViewModel = viewModel()
){

    val scrollState = rememberScrollState()
    val abel = FontFamily(Font(R.font.abelregular))

    var email by remember { mutableStateOf("") }
    var correoValido by remember { mutableStateOf(false) }

    var code by remember { mutableStateOf("") }
    var codigoEnviado by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val loading by resetPasswordViewModel.loading.collectAsState()
    val error by resetPasswordViewModel.error.collectAsState()
    val successMessage by resetPasswordViewModel.successMessage.collectAsState()

    LaunchedEffect(successMessage) {
        if (successMessage == "Contraseña actualizada correctamente") {
            navController.navigate(LoginScreenNavigation) {
                popUpTo(0)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logoreubica),
            contentDescription = "Logo"
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cambiar contraseña",
            color = Color(0xFF5A3C1D),
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            fontFamily = abel
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = buildAnnotatedString {
                append("Restablece tu contraseña de forma segura y rápida.\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("¡No olvides elegir una contraseña segura!")
                }
            },
            color = Color(0xFF5A3C1D),
            fontSize = 16.sp,
            fontFamily = abel,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column (
            modifier = Modifier.fillMaxWidth().padding(25.dp)
        ){
            Text(
                text = "Correo electrónico",
                color = Color(0xFF5A3C1D),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = abel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    correoValido = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                },
                placeholder = { Text("Ingrese dirección de correo", fontFamily = abel, color = Color.Black.copy(alpha = 0.5f)) },
                shape = RectangleShape,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                textStyle = LocalTextStyle.current.copy(color = Color.Black, fontFamily = abel),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFDFF2E1),
                    unfocusedContainerColor = Color(0xFFDFF2E1),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (!codigoEnviado) {
                Button(
                    onClick = {
                        resetPasswordViewModel.sendResetCode(email)
                    },
                    enabled = correoValido && !loading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    Text("Enviar código", fontFamily = abel, color = Color.White, fontSize = 18.sp)
                }
            } else {
                Text(
                    text = "Código recibido",
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel,
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp)
                )
                TextField(
                    value = code,
                    onValueChange = { code = it },
                    placeholder = { Text("Ingrese el código", fontFamily = abel, color = Color.Black.copy(alpha = 0.5f)) },
                    shape = RectangleShape,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black, fontFamily = abel),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFDFF2E1),
                        unfocusedContainerColor = Color(0xFFDFF2E1),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Nueva contraseña",
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel,
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp)
                )
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text("Ingrese nueva contraseña", fontFamily = abel, color = Color.Black.copy(alpha = 0.5f)) },
                    shape = RectangleShape,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black, fontFamily = abel),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFDFF2E1),
                        unfocusedContainerColor = Color(0xFFDFF2E1),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Confirmar nueva contraseña",
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel,
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp)
                )
                TextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    placeholder = { Text("Confirme nueva contraseña", fontFamily = abel, color = Color.Black.copy(alpha = 0.5f)) },
                    shape = RectangleShape,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black, fontFamily = abel),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFDFF2E1),
                        unfocusedContainerColor = Color(0xFFDFF2E1),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (email.isBlank() || code.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
                            errorMessage = "Por favor, complete todos los campos antes de continuar"
                            showError = true
                            return@Button
                        }
                        if (newPassword != confirmNewPassword) {
                            errorMessage = "Las contraseñas no coinciden"
                            showError = true
                            return@Button
                        }

                        errorMessage = null
                        showError = false
                        resetPasswordViewModel.resetPassword(email, code, newPassword, confirmNewPassword)
                    },
                    enabled = !loading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    Text("Confirmar", fontFamily = abel, color = Color.White, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            if (showError && errorMessage != null) {
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
                            text = errorMessage!!,
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

            Spacer(modifier = Modifier.height(20.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (loading) {
                    Text("Cargando...", color = Color.Gray, fontFamily = abel)
                }

                error?.let {
                    Text(text = it, color = Color.Red, fontFamily = abel)
                }

                successMessage?.let {
                    Text(text = it, color = Color(0xFF49724C), fontFamily = abel)
                }

                if (successMessage == "Código enviado" || successMessage?.contains("Código enviado") == true) {
                    codigoEnviado = true
                }
            }
        }
    }
}