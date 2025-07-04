package com.proyecto.ReUbica.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.ReUbica.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {

    TopAppBar(
        title = {

            Text(
                text = "ReUbica",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF5A3C1D),
                modifier = Modifier.offset(x = (-15).dp)
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp).padding(start = 5.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDFF2E1)
        )
    )
}