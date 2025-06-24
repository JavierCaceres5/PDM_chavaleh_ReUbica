package com.proyecto.ReUbica.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.navigations.MainNavigationRoute

@Composable
fun LoginScreen(navController: NavHostController) {
    val abel = FontFamily(Font(R.font.abelregular))
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Image(painter = painterResource(R.drawable.logoreubica), contentDescription = "Logo")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color(0xFF5A3C1D), fontFamily = abel)

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            buildAnnotatedString {
                append("Accede fácilmente a negocios locales,\nproductos y servicios cerca de vos, ")
                append("todo desde tu celular")
            },
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF5A3C1D),
            fontFamily = abel,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text("Correo electrónico", fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, color = Color(0xFF5A3C1D), modifier = Modifier.fillMaxWidth())

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
            Text("Contraseña", fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, color = Color(0xFF5A3C1D))
            Text("Olvidé mi contraseña", fontSize = 14.sp, color = Color(0xFF5A3C1D), fontFamily = abel)
        }

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Ingrese su contraseña", fontFamily = abel, color = Color.Black.copy(alpha = 0.5f)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null, tint = Color.Black)
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

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { navController.navigate(MainNavigationRoute) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            Text("Iniciar sesión", fontFamily = abel, color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
