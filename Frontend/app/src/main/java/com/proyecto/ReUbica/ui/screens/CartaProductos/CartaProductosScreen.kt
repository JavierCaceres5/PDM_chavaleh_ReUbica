package com.proyecto.ReUbica.ui.screens.CartaProductos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen3Navigation

@Composable
fun CartaProductosScreen(
    navController: NavHostController
) {

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.profile),
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
        }

        Text(
            text = "Tu carta de productos",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A3C1D),
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )


        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate(RegisterLocalScreen3Navigation) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar",
                    tint = Color(0xFF5A3C1D),
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(
                text = "Agregar producto",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 16.sp
            )
        }
    }
}