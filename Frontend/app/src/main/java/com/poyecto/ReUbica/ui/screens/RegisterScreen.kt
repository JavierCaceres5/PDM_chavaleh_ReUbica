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
import com.proyecto.ReUbica.ui.navigations.MainNavigationRoute

@Composable
fun RegisterScreen(navController: NavHostController) {
    val abel = FontFamily(Font(R.font.abelregular))

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(15.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                modifier = Modifier.padding(top = 20.dp)
            )

            Column(
                modifier = Modifier.padding(top = 15.dp)
            ) {
                fun customTextField(
                    value: String,
                    onValueChange: (String) -> Unit,
                    placeholderText: String,
                    isPassword: Boolean = false
                ): @Composable () -> Unit {
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
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            modifier = Modifier.width(300.dp)
                        )
                    }
                }

                Text("Nombre", modifier = Modifier.padding(bottom = 4.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, color = Color(0xFF5A3C1D))
                customTextField(name, { name = it }, "Ingrese su nombre")()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Apellido", modifier = Modifier.padding(bottom = 4.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, color = Color(0xFF5A3C1D))
                customTextField(lastName, { lastName = it }, "Ingrese su apellido")()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Número de teléfono", modifier = Modifier.padding(bottom = 4.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, color = Color(0xFF5A3C1D))
                customTextField(telephone, { telephone = it }, "+503 0000 0000")()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Correo electrónico", modifier = Modifier.padding(bottom = 4.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, color = Color(0xFF5A3C1D))
                customTextField(email, { email = it }, "Ingrese dirección de correo")()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Contraseña", modifier = Modifier.padding(bottom = 4.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, color = Color(0xFF5A3C1D))
                customTextField(password, { password = it }, "Ingrese su contraseña", isPassword = true)()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Confirmar contraseña", modifier = Modifier.padding(bottom = 4.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = abel, color = Color(0xFF5A3C1D))
                customTextField(confirmPassword, { confirmPassword = it }, "Confirme la contraseña", isPassword = true)()
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
                onClick = { navController.navigate(MainNavigationRoute) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp).width(175.dp),
                shape = RectangleShape
            ) {
                Text(text = "Registrarme", fontFamily = abel, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}
