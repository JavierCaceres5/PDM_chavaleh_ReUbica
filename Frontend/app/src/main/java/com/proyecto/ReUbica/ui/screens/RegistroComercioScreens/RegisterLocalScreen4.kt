package com.proyecto.ReUbica.ui.screens.RegistroComercioScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.layouts.StepTopBar
import com.proyecto.ReUbica.ui.navigations.HomeScreenNavigation


@Composable
fun RegisterLocalScreen4(navController: NavHostController) {
    RegisterLocalScreen4Content(
        navController = navController,
        onBack = { navController.popBackStack() }
    )
}


@Composable
fun RegisterLocalScreen4Content(
    navController: NavHostController,
    onBack: () -> Unit = {}
) {
    val abel = FontFamily(Font(R.font.abelregular))
    val poppins = FontFamily(Font(R.font.poppinsextrabold))
    var showConfirmation by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        StepTopBar(step = 2, title = "Carta de productos", onBackClick = onBack)

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
                text = "¡Continuemos con tu registro!",
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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tu Carta de productos",
                fontFamily = poppins,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Start),
                color = Color(0xFF5A3C1D)
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProductoItem(
                imageRes = R.drawable.pupusas,
                titulo = "Combo de 5 pupusas",
                descripcion = "Tres pupusas de frijol y dos de queso.",
                precio = "$5.99"
            )

            Divider(color = Color(0xFF49724C), thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))

            ProductoItem(
                imageRes = R.drawable.yuca,
                titulo = "Plato de yuca frita",
                descripcion = "Porción de yuca frita doradita, con curtido y salsa criolla.",
                precio = "$3.50"
            )

            Divider(color = Color(0xFF49724C), thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { showConfirmation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Publicar", color = Color.White, fontFamily = abel)
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (showConfirmation) {
                AlertDialog(
                    onDismissRequest = { showConfirmation = false },
                    confirmButton = {
                        OutlinedButton(
                            onClick = {
                                showConfirmation = false
                                navController.navigate(HomeScreenNavigation) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            border = ButtonDefaults.outlinedButtonBorder,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Aceptar",
                                fontFamily = FontFamily(Font(R.font.poppinsextrabold)),
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "¡Tu local fue registrado con éxito!",
                            fontFamily = poppins,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Black
                        )
                    },
                    text = {
                        Text(
                            "Tu carta de productos ya está disponible y los clientes podrán ver lo que ofreces en ReUbica. Ahora estás listo para comenzar a recibir pedidos y hacer crecer tu negocio.",
                            fontFamily = abel,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Black
                        )
                    },
                    containerColor = Color.White,
                    tonalElevation = 6.dp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}

@Composable
fun ProductoItem(imageRes: Int, titulo: String, descripcion: String, precio: String) {
    val poppins = FontFamily(Font(R.font.poppinsextrabold))
    val abel = FontFamily(Font(R.font.abelregular))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = titulo,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(titulo, fontFamily = poppins, fontSize = 14.sp, color = Color(0xFF5A3C1D))
            Text(descripcion, fontFamily = abel, fontSize = 12.sp, color = Color.Black)
        }

        Text(precio, fontFamily = poppins, fontSize = 14.sp, color = Color(0xFF5A3C1D))
    }
}
