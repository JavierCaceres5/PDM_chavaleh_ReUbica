package com.proyecto.ReUbica.ui.screens

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun TerminosYcondiciones(navController: NavHostController) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp).verticalScroll(scrollState)
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
            text = "Términos y condiciones",
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 40.dp).padding(bottom = 15.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = buildAnnotatedString {
                append("Al utilizar esta aplicación, aceptás nuestros Términos y Condiciones y nuestra Política de Privacidad. Nos comprometemos a proteger tus datos y garantizar una experiencia segura.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("1. Aceptación de los Términos\n")
                }
                append("Al descargar, registrarte y utilizar nuestra aplicación móvil, aceptas cumplir con estos Términos y Condiciones. Si no estás de acuerdo, por favor no utilices la aplicación.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("2. Descripción del Servicio\n")
                }
                append("Nuestra aplicación permite a los usuarios crear perfiles para promocionar su negocio o emprendimiento de forma gratuita, incluyendo la publicación de información como ubicación, productos/servicios ofrecidos, imágenes, datos de contacto, entre otros.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("3. Uso Permitido\n")
                }
                append("El usuario se compromete a utilizar la aplicación únicamente con fines lícitos y a no publicar contenido ofensivo, falso o ilegal.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("4. Contenido del Usuario\n")
                }
                append("Eres responsable del contenido que publiques. Nos reservamos el derecho de eliminar cualquier contenido que consideremos inapropiado o que incumpla estos términos.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("5. Propiedad Intelectual\n")
                }
                append("La aplicación, su diseño, estructura y código son propiedad de los desarrolladores del proyecto. El contenido publicado por los usuarios sigue siendo propiedad de cada uno, pero otorgas a la app un derecho no exclusivo para mostrarlo dentro de la plataforma.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("6. Limitación de Responsabilidad\n")
                }
                append("No somos responsables por la veracidad de la información publicada por los usuarios ni por cualquier transacción que se realice fuera de la aplicación. La app es solo una herramienta de promoción.\n\n")

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("7. Modificaciones\n")
                }
                append("Nos reservamos el derecho a modificar estos Términos en cualquier momento. Te notificaremos si hay cambios importantes.\n")
            },
            fontSize = 15.sp,
            modifier = Modifier.padding(10.dp),
            textAlign = TextAlign.Justify
        )
    }
}
