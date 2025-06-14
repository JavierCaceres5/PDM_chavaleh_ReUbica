package com.poyecto.ReUbica.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.ReUbica.R

@Composable
fun RestaurantCard(
    nombre: String,
    departamento: String,
    categoria: String,
    imagenRes: Int,
    isFavorito: Boolean,
    onFavoritoClick: () -> Unit,
    onVerTiendaClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(265.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8EF))
    )
    {
        Column(modifier = Modifier.padding(5.dp)) {

            Row(
                Modifier.fillMaxWidth().padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = categoria,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .background(Color(0xFFF7F4CC), RoundedCornerShape(50))
                        .padding(4.dp)
                        .padding(start = 5.dp, end = 5.dp)
                )
                Icon(
                    imageVector = if (isFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = Color(0xFF5A3C1D),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onFavoritoClick() }
                )

            }
            Row(
                Modifier.fillMaxWidth().padding(start = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(imagenRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFDFF2E1))
                )

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF5A3C1D),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(departamento, fontSize = 13.sp, color = Color.Gray)
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = onVerTiendaClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49724C)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(3.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Ver tienda", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}