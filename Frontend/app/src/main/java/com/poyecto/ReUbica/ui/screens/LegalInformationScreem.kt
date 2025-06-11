package com.poyecto.ReUbica.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.poyecto.ReUbica.ui.Components.ListItemRow
import com.proyecto.ReUbica.ui.navigations.PoliticaDePrivacidadNavigation
import com.proyecto.ReUbica.ui.navigations.TerminosYCondicionesNavigation

@Composable
fun LegalInformationScreen(navController: NavHostController) {

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
    ){

        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        Text(
            text = "Información legal",
            fontSize = 25.sp,
            modifier = Modifier.padding(15.dp)
        )

        Text(
            text = "Al utilizar esta aplicación, aceptás nuestros Términos y Condiciones y nuestra Política de Privacidad. Nos comprometemos a proteger tus datos y garantizar una experiencia segura. ",
            Modifier.padding(10.dp),
            fontSize = 15.sp,
            textAlign = TextAlign.Justify
        )
        Text( text = "Toda la información presentada en la app es únicamente con fines informativos. Nos reservamos el derecho de modificar los contenidos y condiciones en cualquier momento.",
            Modifier.padding(10.dp),
            fontSize = 15.sp,
            textAlign = TextAlign.Justify
        )
        ListItemRow(
            "Términos y condiciones",
            onClick = {navController.navigate(TerminosYCondicionesNavigation)},
            icon = Icons.Filled.ArrowOutward
        )
        ListItemRow(
            "Política de privacidad",
            onClick = {navController.navigate(PoliticaDePrivacidadNavigation)},
            icon = Icons.Filled.ArrowOutward
        )

    }
}

@Preview(showBackground = true)
@Composable
fun LegalInformationScreenPreview() {
    LegalInformationScreen(navController = rememberNavController())
}