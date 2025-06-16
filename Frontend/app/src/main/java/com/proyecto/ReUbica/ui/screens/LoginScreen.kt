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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.navigations.HomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.mainNavigation

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
            .padding(horizontal = 20.dp).padding(top = 35.dp).padding(25.dp),
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
                modifier = Modifier.padding(end = 10.dp)
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

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {navController.navigate(mainNavigation)},
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