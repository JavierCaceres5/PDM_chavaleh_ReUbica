package com.poyecto.ReUbica.ui.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.toString

@Composable
fun BottomBar(
    navItems: List<navItem>, selectedItem: String = "nowplaying", onItemSelected: (String) -> Unit
){
    Column {
        HorizontalDivider(color = Color(0xFFBDBDBD), thickness = 1.dp)
        NavigationBar(
            containerColor = Color.White
        ){
            navItems.forEach { item ->
                NavigationBarItem(
                    selected = selectedItem == item.route,
                    onClick = { onItemSelected(item.route.toString()) },
                    icon = { Icon(item.icon, contentDescription = item.title, modifier = Modifier.size(25.dp), tint = Color.Black) },
                    label = { Text(item.title, fontWeight = FontWeight.Bold, color = Color.Black) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black
                    )
                )
            }
        }
    }
}