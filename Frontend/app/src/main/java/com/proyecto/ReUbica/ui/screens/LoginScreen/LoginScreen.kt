package com.proyecto.ReUbica.ui.screens.LoginScreen

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.navigations.LoginScreenNavigation
import com.proyecto.ReUbica.ui.navigations.RegistroNavigation
import com.proyecto.ReUbica.ui.navigations.ResetPasswordScreenNavigation
import com.proyecto.ReUbica.ui.navigations.WelcomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.mainNavigation
import com.proyecto.ReUbica.ui.screens.RegisterScreen.RegisterScreenViewModel
import com.proyecto.ReUbica.ui.screens.ResetPassword.ResetPasswordScreen

@Composable
fun LoginScreen(
    navController: NavHostController,
) {

    val abel = FontFamily(Font(R.font.abelregular))
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    val loginViewModel: LoginScreenViewModel = viewModel()
    val userViewModel: RegisterScreenViewModel = viewModel()

    val user by loginViewModel.user.collectAsState()
    val error by loginViewModel.error.collectAsState()
    val loading by loginViewModel.loading.collectAsState()

    val emailValidations = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    var navigated by remember { mutableStateOf(false) }

    fun isValidEmail(email: String): Boolean = emailValidations.matches(email)

    if (user != null && !navigated) {
        LaunchedEffect(user) {
            navigated = true
            userViewModel.setUser(user!!)
            navController.navigate(mainNavigation) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(color = Color(0xFF49724C))
        }
        return
    } else if (user != null && navigated) {
        Box(modifier = Modifier.fillMaxSize()) {}
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 35.dp)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(R.drawable.logoreubica),
            contentDescription = "Logo"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Iniciar Sesión",
            color = Color(0xFF5A3C1D),
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            fontFamily = abel
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = buildAnnotatedString {
                append("Accede fácilmente a negocios locales,\nproductos y servicios cerca de vos, ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("todo desde tu celular")
                }
            },
            color = Color(0xFF5A3C1D),
            fontSize = 16.sp,
            fontFamily = abel,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

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
            onValueChange = { email = it },
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Contraseña",
                color = Color(0xFF5A3C1D),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = abel,
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = "Olvide mi contraseña",
                color = Color(0xFF5A3C1D),
                fontSize = 14.sp,
                fontFamily = abel,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable {
                        navController.navigate(ResetPasswordScreenNavigation)
                    }
            )
        }

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Ingrese su contraseña", fontFamily = abel, color = Color.Black.copy(alpha = 0.5f)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                        tint = Color.Black
                    )
                }
            },
            shape = RectangleShape,
            singleLine = true,
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

        val showError = errorMessage ?: error
        if (showError != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    text = showError,
                    color = Color(0xFFD32F2F),
                    fontSize = 14.sp,
                    fontFamily = abel,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                errorMessage = when {
                    email.isBlank() -> "Por favor, ingrese su correo electrónico."
                    password.isBlank() -> "Por favor, ingrese su contraseña."
                    !isValidEmail(email) -> "Ingrese un correo electrónico válido."
                    else -> null
                }
                if (errorMessage == null) {
                    loginViewModel.login(email, password)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            Text("Iniciar sesión", fontFamily = abel, color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))

        val annotatedText = buildAnnotatedString {
            append("No tienes una cuenta? ", )
            val start = length
            append("Registrate aquí")
            addStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 16.sp,
                    fontFamily = abel
                ),
                start,
                length
            )
            addStringAnnotation("register", "register", start, length)
        }
        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                val annotations = annotatedText.getStringAnnotations("register", offset, offset)
                if (annotations.isNotEmpty()) {
                    navController.navigate(RegistroNavigation)
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
