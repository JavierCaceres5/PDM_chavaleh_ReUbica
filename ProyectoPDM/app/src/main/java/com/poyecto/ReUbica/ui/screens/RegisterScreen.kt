package com.proyecto.ReUbica.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.R

@Composable
fun RegisterScreen(navController: NavHostController) {
    val abel = FontFamily(Font(R.font.abelregular))

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var email by remember {mutableStateOf("")}
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.padding(15.dp).verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painterResource(R.drawable.logoreubica),
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
            Column (
                modifier = Modifier.padding(top = 15.dp).padding(bottom = 20.dp)
            ) {

                fun customTextField(value: String, onValueChange: (String) -> Unit, placeholderText: String, isPassword: Boolean = false): @Composable () -> Unit {
                    return {
                        TextField(
                            value = value,
                            onValueChange = onValueChange,
                            placeholder = {
                                Text(
                                    text = placeholderText,
                                    color = Color.Gray,
                                    fontFamily = abel,
                                    fontSize = 14.sp
                                )
                            },
                            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                            trailingIcon = if (isPassword) {
                                {
                                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(imageVector = image, contentDescription = null, tint = Color.Black)
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
                }

                Text("Nombre", color = Color(0xFF5A3C1D), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, modifier = Modifier.padding(bottom = 4.dp))
                customTextField(name, { name = it }, "Ingrese su nombre")()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Apellido", color = Color(0xFF5A3C1D), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, modifier = Modifier.padding(bottom = 4.dp))
                customTextField(lastName, { lastName = it }, "Ingrese su apellido")()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Número de teléfono", color = Color(0xFF5A3C1D), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, modifier = Modifier.padding(bottom = 4.dp))
                customTextField(telephone, { telephone = it }, "+503 0000 0000")()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Correo electrónico", color = Color(0xFF5A3C1D), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, modifier = Modifier.padding(bottom = 4.dp))
                customTextField(email, { email = it }, "Ingrese dirección de correo")()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Contraseña", color = Color(0xFF5A3C1D), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, modifier = Modifier.padding(bottom = 4.dp))
                customTextField(password, { password = it }, "Ingrese su contraseña", isPassword = true)()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Confirmar contraseña", color = Color(0xFF5A3C1D), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, modifier = Modifier.padding(bottom = 4.dp))
                customTextField(confirmPassword, { confirmPassword = it }, "Confirme la contraseña", isPassword = true)()
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                )
                Text(
                    text = "Autorizo el uso de esta información para futuros canales de comunicación.",
                    color = Color(0xFF5A3C1D),
                    fontSize = 14.sp,
                    fontFamily = abel,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C), contentColor = Color.White),
                modifier = Modifier.padding(10.dp).width(175.dp).padding(top = 15.dp),
                shape = RectangleShape
            ) {
                Text(text = "Registrarme", fontFamily = abel)
            }
        }
    }
}