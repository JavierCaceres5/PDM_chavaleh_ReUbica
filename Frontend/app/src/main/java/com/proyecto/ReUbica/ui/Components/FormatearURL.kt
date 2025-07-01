package com.proyecto.ReUbica.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormatearURL(
    url: String?,
    nombreRed: String,
    iconId: Int,
    onEditarClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDFF2E1), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = nombreRed,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = nombreRed,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (url.isNullOrBlank()) {
                Text(
                    text = "No has agregado tu cuenta aún",
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
            } else {
                val resumirUrl = url.substringBefore("?")
                    .replace("https://", "")
                    .replace("http://", "")
                    .replace("www.", "")

                Text(
                    text = resumirUrl,
                    color = Color(0xFF013F6D),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { uriHandler.openUri(url) }
                )
            }

            IconButton(
                onClick = onEditarClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar $nombreRed",
                    tint = Color(0xFF5A3C1D)
                )
            }
        }
    }
}

