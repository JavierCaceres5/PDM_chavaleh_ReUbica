package com.proyecto.ReUbica.ui.screens.RegistroComercioScreens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.layouts.CustomScaffold
import com.proyecto.ReUbica.ui.layouts.StepTopBar
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen4Navigation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.BorderStroke
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen2Navigation

@Composable
fun RegisterLocalScreen3(navController: NavHostController) {
    RegisterLocalScreen3Content(
        onNext = { navController.navigate(RegisterLocalScreen4Navigation) },
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun RegisterLocalScreen3Content(
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var contadorProductos by remember { mutableStateOf(0) }

    val abel = FontFamily(Font(R.font.abelregular))
    val poppins = FontFamily(Font(R.font.poppinsextrabold))

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    val camposValidos = nombre.isNotBlank() && descripcion.isNotBlank() && precio.isNotBlank()

    Column(Modifier.fillMaxSize()) {
        StepTopBar(step = 1, title = "Carta de productos", onBackClick = onBack)

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Comencemos con tus productos!",
                fontFamily = poppins,
                fontSize = 24.sp,
                color = Color(0xFF5A3C1D),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Esta información será visible en tu perfil y permitirá a los clientes conocer lo que ofreces en tu negocio.",
                fontFamily = abel,
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Label afuera del campo
            Text("Nombre del producto", fontFamily = poppins, fontSize = 14.sp, color = Color(0xFF5A3C1D), modifier = Modifier.fillMaxWidth())
            ProductInputField("Nombre del producto", nombre) { nombre = it }
            Spacer(modifier = Modifier.height(12.dp))

            Text("Descripción del producto", fontFamily = poppins, fontSize = 14.sp, color = Color(0xFF5A3C1D), modifier = Modifier.fillMaxWidth())
            ProductInputField("Descripción del producto", descripcion) { descripcion = it }
            Spacer(modifier = Modifier.height(12.dp))

            Text("Precio", fontFamily = poppins, fontSize = 14.sp, color = Color(0xFF5A3C1D), modifier = Modifier.fillMaxWidth())
            ProductInputField("0.00", precio, isPrice = true) { precio = it }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Cantidad de Productos ingresados",
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contadorProductos.toString(),
                onValueChange = {},
                enabled = false,
                textStyle = LocalTextStyle.current.copy(fontFamily = abel, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFDFF2E1), RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledBorderColor = Color.Transparent,
                    disabledContainerColor = Color(0xFFDFF2E1)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Adjunte foto del producto",
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.fillMaxWidth()
            )


            Text(
                text = "Asegúrese de que el menú adjunto se vea correctamente. Sólo archivos con formatos JPEG, PDF o PNG podrán ser convertidos",
                fontFamily = abel,
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                    .clickable { launcher.launch("image/*") }
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Upload,
                            contentDescription = "Subir",
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                        Text("Seleccione una imagen", fontFamily = abel, fontSize = 12.sp, color = Color.Black)
                    }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        contadorProductos++
                        nombre = ""
                        descripcion = ""
                        precio = ""
                        imageUri = null
                        showDialog = true
                    },
                    enabled = camposValidos,
                    colors = ButtonDefaults.buttonColors(containerColor = if (camposValidos) Color(0xFF49724C) else Color.Gray),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Agregar", color = Color.White, fontFamily = abel)
                }

                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Continuar", color = Color.White, fontFamily = abel)
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        "¡Todo listo!",
                        fontFamily = poppins,
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Text(
                        "Tu producto ha sido publicado con éxito.",
                        fontFamily = abel,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                confirmButton = {
                    OutlinedButton(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Continuar", fontFamily = poppins)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            // Revertir el conteo
                            if (contadorProductos > 0) contadorProductos--
                            showDialog = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8B1A1A),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancelar", fontFamily = poppins)
                    }
                },
                containerColor = Color.White,
                tonalElevation = 6.dp,
                shape = RoundedCornerShape(12.dp)
            )
        }

    }
}

@Composable
fun ProductInputField(
    placeholder: String,
    value: String,
    isPrice: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val abel = FontFamily(Font(R.font.abelregular))

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (isPrice) {
                val cleanValue = it.replace("[^\\d.]".toRegex(), "")
                onValueChange(cleanValue)
            } else {
                onValueChange(it)
            }
        },
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = abel,
                color = if (isPrice) Color(0xFF8AA09A) else Color.Black
            )
        },
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontFamily = abel
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDFF2E1), RoundedCornerShape(8.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color(0xFFDFF2E1),
            unfocusedContainerColor = Color(0xFFDFF2E1),
            cursorColor = Color.Black
        ),
        leadingIcon = if (isPrice) {
            {
                Text(
                    text = "$",
                    fontFamily = abel,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        } else null,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = if (isPrice) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
    )
}

