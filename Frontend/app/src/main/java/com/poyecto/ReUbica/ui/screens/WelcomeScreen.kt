package com.proyecto.ReUbica.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import com.proyecto.ReUbica.ui.navigations.LoginScreenNavigation
import com.proyecto.ReUbica.ui.navigations.RegistroNavigation
import com.proyecto.ReUbica.R

@Composable
fun WelcomeScreen(navController: NavHostController) {
    val abel = FontFamily(Font(R.font.abelregular))
    val scrollState = rememberScrollState()

    val images = listOf(
        R.drawable.reubica,
        R.drawable.reubica,
        R.drawable.reubica,
        R.drawable.reubica,
        R.drawable.reubica,
        R.drawable.reubica,
        R.drawable.reubica,
        R.drawable.reubica,
        R.drawable.reubica
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .padding(top = 65.dp, bottom = 30.dp)
        ) {
            Column {
                var index = 0
                repeat(3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(3) {
                            val imageRes = images[index]
                            index++
                            Card(
                                modifier = Modifier
                                    .height(130.dp)
                                    .width(97.dp)
                                    .padding(4.dp)
                                    .padding(bottom = 8.dp)
                                    .offset(y = if (it == 0 || it == 2) 40.dp else 0.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFDFF2E1))
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Image(
                                        painter = painterResource(id = imageRes),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(0.9f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Conectamos negocios locales contigo",
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                fontFamily = abel,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Descubre negocios locales, apoya a tu comunidad y encuentra justo lo que necesitas.",
                color = Color(0xFF5A3C1D),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = abel,
                modifier = Modifier.padding(18.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { navController.navigate(RegistroNavigation) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Empecemos", color = Color.White, fontFamily = abel, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(15.dp))

            val annotatedText = buildAnnotatedString {
                append("Ya tienes una cuenta? ")
                val start = length
                append("Inicia Sesión")
                addStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5A3C1D),
                        fontSize = 16.sp,
                        fontFamily = abel
                    ),
                    start,
                    length
                )
                addStringAnnotation("login", "login", start, length)
            }
            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    val annotations = annotatedText.getStringAnnotations("login", offset, offset)
                    if (annotations.isNotEmpty()) {
                        navController.navigate(LoginScreenNavigation)
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}