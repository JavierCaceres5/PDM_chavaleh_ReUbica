package com.proyecto.ReUbica.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.ui.Components.ListItemRow

@Composable
fun LocalInformationScreen(navController: NavHostController) {

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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = "Nombre del local",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )
                ListItemRow(
                    text = "",
                    onClick = {},
                    icon = Icons.Filled.Edit,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Descripción del local",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )
                ListItemRow(
                    text = "",
                    onClick = {},
                    icon = Icons.Filled.Edit,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Sucursal",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )
                ListItemRow(
                    text = "",
                    onClick = {},
                    icon = Icons.Filled.Edit,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Horario de atención",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )
                ListItemRow(
                    text = "",
                    onClick = {},
                    icon = Icons.Filled.Edit,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Tipo de negocio",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )
                ListItemRow(
                    text = "",
                    onClick = {},
                    icon = Icons.Filled.Edit,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Dirección completa del local",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )
                ListItemRow(
                    text = "",
                    onClick = {},
                    icon = Icons.Filled.Edit,
                )
            }
        }
    }
}