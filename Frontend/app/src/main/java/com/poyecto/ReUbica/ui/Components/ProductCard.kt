package com.poyecto.ReUbica.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poyecto.ReUbica.data.Producto
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight


@Composable
fun ProductCard(product: Producto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
        ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Box(modifier = Modifier.size(80.dp).background(Color.LightGray))
            Spacer(Modifier.width(8.dp))
            Column {
                Text(product.name, style = MaterialTheme.typography.titleMedium, color = Color(0xFF5A3C1D), fontWeight = FontWeight.ExtraBold)
                Text(product.description, maxLines = 2, overflow = TextOverflow.Ellipsis, color = Color(0xFF5A3C1D))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$${product.price}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5A3C1D)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Icon(imageVector = Icons.Default.MailOutline,contentDescription = "correo",modifier = Modifier,tint = Color(0xFF5A3C1D))
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Calificacion", tint = Color(0xFF5A3C1D))
                    Text(text = product.rating.toString() , color = Color(0xFF5A3C1D))
                }
            }
        }
    }
}
