package com.proyecto.ReUbica.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.ReUbica.R

@Composable
fun StepTopBar(
    step: Int,
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFDFF2E1))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Volver",
            tint = Color(0xFF5A3C1D),
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.width(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            StepCircle(1, step == 1)
            Spacer(modifier = Modifier.width(10.dp))
            StepCircle(2, step == 2)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.poppinsextrabold)),
            fontSize = 14.sp,
            color = Color(0xFF5A3C1D)
        )
    }
}

@Composable
private fun StepCircle(step: Int, active: Boolean) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(if (active) Color(0xFF49724C) else Color(0xFFDFF2E1)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = step.toString(),
            fontFamily = FontFamily(Font(R.font.poppinsextrabold)),
            color = if (active) Color.White else Color(0xFF49724C),
            fontSize = 13.sp
        )
    }
}
