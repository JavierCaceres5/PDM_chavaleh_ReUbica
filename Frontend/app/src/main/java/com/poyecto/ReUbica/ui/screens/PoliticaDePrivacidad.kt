package com.poyecto.ReUbica.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.poyecto.ReUbica.ui.Components.Viñeta

@Composable
fun PoliticaDePrivacidad(navController: NavHostController) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
            .verticalScroll(scrollState)
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        Text(
            text = "Política de privacidad",
            fontSize = 25.sp,
            modifier = Modifier
                .padding(start = 40.dp)
                .padding(bottom = 15.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "1. Información que Recopilamos",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
            textAlign = TextAlign.Justify
        )
        Viñeta("Nombre del usuario o nombre comercial")
        Viñeta("Información de contacto (correo, teléfono, dirección del negocio)")
        Viñeta("Imágenes del local o productos")
        Viñeta("Ubicación (si se habilita el GPS)")

        Text(
            text = "2. Uso de la Información",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
            textAlign = TextAlign.Justify
        )
        Text(
            text = "La información será utilizada únicamente para mostrarla dentro del perfil del negocio en la aplicación, con fines de promoción.",
            fontSize = 15.sp,
            textAlign = TextAlign.Justify
        )

        Text(
            text = "3. Almacenamiento y Seguridad",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
            textAlign = TextAlign.Justify
        )
        Text(
            text = "Los datos serán almacenados de forma segura en servidores o bases de datos protegidas. No compartiremos tu información con terceros sin tu consentimiento.",
            fontSize = 15.sp,
            textAlign = TextAlign.Justify
        )

        Text(
            text = "4. Derechos del Usuario",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
            textAlign = TextAlign.Justify
        )
        Viñeta("Acceder a tu información")
        Viñeta("Modificarla o eliminarla")
        Viñeta("Solicitar que eliminemos completamente tu cuenta y datos")

        Text(
            text = "5. Publicidad y Cookies",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
            textAlign = TextAlign.Justify
        )
        Text(
            text = "No utilizamos cookies ni publicidad invasiva. En caso de integrar algún sistema de anuncios en el futuro, te notificaremos con anticipación y te pediremos consentimiento.",
            fontSize = 15.sp,
            textAlign = TextAlign.Justify
        )
    }
}
