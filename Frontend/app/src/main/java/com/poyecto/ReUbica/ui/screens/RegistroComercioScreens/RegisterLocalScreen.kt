package com.poyecto.ReUbica.ui.screens.RegistroComercioScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.poyecto.ReUbica.ui.Components.Viñeta
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.navigations.RegistroNavigation

@Composable
fun RegisterLocal(navController: NavHostController){

    val abel = FontFamily(Font(R.font.abelregular))


    Column {

        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.padding(start = 20.dp, top = 15.dp)
            )
        }

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painterResource(R.drawable.logoreubica),
                contentDescription = "Logo",
                modifier = Modifier.width(100.dp).height(100.dp)
            )
            Text(
                text = "Registra tu local",
                color = Color(0xFF5A3C1D),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 0.dp)
            )

            Text(
                text = buildAnnotatedString {
                    append("Empieza a vender en la app delivery online ReUbica, ")
                    val start = length
                    append("el mejor canal de ventas para tu local.")
                    addStyle(
                        SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF5A3C1D),
                            fontSize = 15.sp,
                        ),
                        start,
                        length
                    )
                    addStringAnnotation("Ventas", "Local", start, length)
                },
                fontSize = 15.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Text(
                    text = "Pasos a seguir",
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = abel,
                    color = Color(0xFF5A3C1D),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 10.dp),
                    textAlign = TextAlign.Start
                )
                Viñeta("Llena los datos básicos (nombre, tipo de negocio, descripción).")
                Viñeta("Agrega la dirección y sucursales si tienes más de una.")
                Viñeta("Indica tus horarios.")
                Viñeta("Agrega tus redes sociales para que te encuentren fácil.")
                Viñeta("Agrega tu carta de productos.")
                Viñeta("¡Listo! Publica tu negocio y empieza a recibir visitas.")
            }

            Button(
                modifier = Modifier.width(200.dp),
                onClick = {  },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Comenzar registro", color = Color.White, fontFamily = abel, fontSize = 20.sp)
            }

        }
    }
}