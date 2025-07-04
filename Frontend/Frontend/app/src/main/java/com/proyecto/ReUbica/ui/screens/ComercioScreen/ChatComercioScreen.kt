       package com.proyecto.ReUbica.ui.screens.ComercioScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatComercioScreen(
    navController: NavHostController,
    businessName: String,
    phone: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = businessName, color = Color(0xFF5A3C1D))
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Call, contentDescription = null, tint = Color.Black)
                        Text(text = phone, style = MaterialTheme.typography.bodyMedium)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDFF1DC))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp, 8.dp, 16.dp, 0.dp)
        ) {
            // Mensajes simulados
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ChatBubble(text = "Hola, estoy interesado en el collar. ¿Cuál es la longitud de este?", isUser = true)
                ChatBubble(text = "¡Hola! El collar tiene una longitud de 45 cm", isUser = false)
            }

            Spacer(modifier = Modifier.weight(1f))

            var mensaje by remember { mutableStateOf("") }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = mensaje,
                    onValueChange = { mensaje = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe tu mensaje") },
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { /* acción para enviar */ },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF49724C), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Enviar mensaje",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(text: String, isUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isUser) Color(0xFFE0E0E0) else Color(0xFFDFF1DC),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(12.dp),
                color = Color.Black
            )
        }
    }
}
