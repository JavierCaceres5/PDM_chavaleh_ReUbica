package com.proyecto.ReUbica.ui.Components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Viñeta(text: String) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, bottom = 4.dp)
    ) {
        Text(
            text = "•",
            fontSize = 15.sp,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = text,
            fontSize = 15.sp
        )
    }
}
