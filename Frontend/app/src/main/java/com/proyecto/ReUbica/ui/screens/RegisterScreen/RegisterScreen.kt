package com.proyecto.ReUbica.ui.screens.RegisterScreen

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.data.model.user.UserRegisterRequest
import com.proyecto.ReUbica.ui.navigations.WelcomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.mainNavigation

@Composable
fun RegisterScreen(navController: NavHostController) {

    val abel = FontFamily(Font(R.font.abelregular))

    val userViewModel: RegisterScreenViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()
    val user by userViewModel.user.collectAsState()

    val emailValidations = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    val passwordValidations = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?\\-]).{8,}$")

    var navigated by remember { mutableStateOf(false) }

    fun isValidEmail(email: String): Boolean = emailValidations.matches(email)
    fun isValidPassword(password: String): Boolean = passwordValidations.matches(password)

    val loading by userViewModel.loading.collectAsState()

    if (user != null && !navigated) {
        LaunchedEffect(user) {
            navigated = true
            navController.navigate(mainNavigation) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (user != null && navigated) {
        Box(modifier = Modifier.fillMaxSize()) {}
        return
    }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(color = Color(0xFF49724C))
        }
        return
    }

    Column(
        modifier = Modifier
            .padding(15.dp)
            .verticalScroll(scrollState),
    ) {

        IconButton(onClick = { navController.navigate(WelcomeScreenNavigation) }) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.padding(top = 20.dp, start = 15.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logoreubica),
                contentDescription = "Logo"
            )
            Text(
                text = "Regístrate",
                color = Color(0xFF5A3C1D),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                fontFamily = abel,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = buildAnnotatedString {
                    append("Descubre negocios, apoya lo local y encuentra lo que necesitas, ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("todo en un solo lugar")
                    }
                },
                color = Color(0xFF5A3C1D),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = abel,
                modifier = Modifier.padding(4.dp).padding(top = 20.dp)
            )

            Box(
                modifier = Modifier
                    .height(46.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = abel,
                    textAlign = TextAlign.Center
                )
            }
            Column(
                modifier = Modifier.padding(top = 15.dp).padding(bottom = 20.dp)
            ) {

                fun customTextField(
                    value: Any,
                    onValueChange: (Any) -> Unit,
                    labelText: String,
                    isPassword: Boolean = false
                ): @Composable () -> Unit {
                    return {
                        when (value) {
                            is String -> {
                                TextField(
                                    value = value,
                                    onValueChange = { onValueChange(it) },
                                    placeholder = {
                                        Text(
                                            text = labelText,
                                            color = Color.Gray,
                                            fontFamily = abel,
                                            fontSize = 14.sp
                                        )
                                    },
                                    visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                                    trailingIcon = if (isPassword) {
                                        {
                                            val image =
                                                if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                            IconButton(onClick = {
                                                passwordVisible = !passwordVisible
                                            }) {
                                                Icon(
                                                    imageVector = image,
                                                    contentDescription = null,
                                                    tint = Color.Black
                                                )
                                            }
                                        }
                                    } else null,
                                    shape = RectangleShape,
                                    textStyle = LocalTextStyle.current.copy(
                                        color = Color.Black,
                                        fontFamily = abel,
                                        fontSize = 14.sp
                                    ),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFDFF2E1),
                                        unfocusedContainerColor = Color(0xFFDFF2E1),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent
                                    ),
                                    singleLine = true,
                                    modifier = Modifier.width(300.dp)
                                )
                            }

                            is TextFieldValue -> {
                                TextField(
                                    value = value,
                                    onValueChange = { onValueChange(it) },
                                    placeholder = {
                                        Text(
                                            text = labelText,
                                            color = Color.Gray,
                                            fontFamily = abel,
                                            fontSize = 14.sp
                                        )
                                    },
                                    shape = RectangleShape,
                                    textStyle = LocalTextStyle.current.copy(
                                        color = Color.Black,
                                        fontFamily = abel,
                                        fontSize = 14.sp
                                    ),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFDFF2E1),
                                        unfocusedContainerColor = Color(0xFFDFF2E1),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent
                                    ),
                                    singleLine = true,
                                    modifier = Modifier.width(300.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        }
                    }
                }

                Text(
                    "Nombre",
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel
                )
                customTextField(name, { name = it as String }, "Ingrese su nombre")()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Apellido",
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel
                )
                customTextField(lastName, { lastName = it as String }, "Ingrese su apellido")()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Número de teléfono",
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel,
                )
                customTextField(
                    telephone,
                    { newValue ->
                        val newTextFieldValue = newValue as TextFieldValue
                        var text = newTextFieldValue.text.filter { it.isDigit() }
                        if (text.length > 4) {
                            text = text.substring(0, 4) + "-" + text.substring(4, text.length.coerceAtMost(8))
                        }
                        if (text.length > 9) text = text.substring(0, 9)
                        val selection = TextRange(text.length)
                        telephone = TextFieldValue(text, selection)
                    },
                    "0000-0000"
                )()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Correo electrónico",
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel
                )
                customTextField(
                    email,
                    { email = it as String },
                    "Ingrese dirección de correo"
                )()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Contraseña",
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel
                )
                customTextField(
                    password,
                    { password = it as String },
                    "Ingrese su contraseña",
                    isPassword = true
                )()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Confirmar contraseña",
                    color = Color(0xFF5A3C1D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = abel
                )
                customTextField(
                    confirmPassword,
                    { confirmPassword = it as String },
                    "Confirme la contraseña",
                    isPassword = true
                )()
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Checkbox(checked = checked, onCheckedChange = { checked = it })
                Text(
                    text = "Autorizo el uso de esta información para futuros canales de comunicación.",
                    color = Color(0xFF5A3C1D),
                    fontSize = 14.sp,
                    fontFamily = abel,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            Button(
                onClick = {
                    errorMessage = when {
                        name.isBlank() -> "Por favor, ingrese su nombre."
                        lastName.isBlank() -> "Por favor, ingrese su apellido."
                        telephone.text.length < 9 -> "Por favor, ingrese un número de teléfono válido."
                        !telephone.text.replace("-", "").all { it.isDigit() } -> "El número de teléfono solo puede contener números."
                        email.isBlank() -> "Por favor, ingrese su correo electrónico."
                        password.isBlank() -> "Por favor, ingrese una contraseña."
                        confirmPassword.isBlank() -> "Por favor, confirme su contraseña."
                        !isValidEmail(email) -> "Ingrese un correo electrónico válido."
                        !isValidPassword(password) -> "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial."
                        password != confirmPassword -> "Las contraseñas no coinciden."
                        !checked -> "Debe aceptar los términos para continuar."
                        else -> null
                    }
                    if (errorMessage == null) {
                        val trimmedEmail = email.trim()
                        val userRequest = UserRegisterRequest(
                            name,
                            lastName,
                            telephone.text,
                            trimmedEmail,
                            password,
                            confirmPassword
                        )
                        userViewModel.register(userRequest)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF49724C),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(10.dp).width(175.dp),
                shape = RectangleShape
            ) {
                Text(text = "Registrarme", fontFamily = abel, fontSize = 16.sp)
            }
        }
    }
}