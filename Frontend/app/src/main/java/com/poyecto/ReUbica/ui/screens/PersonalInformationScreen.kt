package com.poyecto.ReUbica.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.poyecto.ReUbica.ui.Components.ListItemRow
import com.proyecto.ReUbica.R

@Composable
fun PersonalInformationScreen(navController: NavHostController) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 30.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(120.dp)
                )

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-20).dp, y = (-36).dp)
                        .size(28.dp)
                        .background(Color(0xFF5A3C1D), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Photo",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(35.dp))

        Column (
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = "Nombre completo",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 15.dp)
            )
            ListItemRow(
                "Nombre",
                onClick = {},
                icon = Icons.Filled.Edit
            )
            ListItemRow(
                "Apellido",
                onClick = {},
                icon = Icons.Filled.Edit
            )

            Text(
                text = "Correo Electrónico",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 15.dp)
            )
            ListItemRow(
                "Tucorreo@gmail.com",
                onClick = {},
                icon = Icons.Filled.Edit
            )

            Text(
                text = "Teléfono",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 15.dp)
            )
            ListItemRow(
                "+XXX XXXX-XXXX",
                onClick = {},
                icon = Icons.Filled.Edit
            )

            Row (
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C), contentColor = Color.White),
                    modifier = Modifier.padding(10.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Editar perfil", fontSize = 16.sp)
                }
            }
        }
    }
}
