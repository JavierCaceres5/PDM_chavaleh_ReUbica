package com.poyecto.ReUbica.ui.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.ui.navigations.*

data class navItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun CustomScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val navItems = listOf(
        navItem("Inicio", Icons.Filled.Home, HomeScreenNavigation.route),
        navItem("Buscar", Icons.Default.Search, SearchScreenNavigation.route),
        navItem("Mi cuenta", Icons.Filled.AccountCircle, ProfileScreenNavigation.route),
        navItem("Favoritos", Icons.Default.Favorite, FavoritesScreenNavigation.route)
    )

    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = {
            BottomBar(
                navItems = navItems,
                selectedItem = selectedItem,
                onItemSelected = {
                    onItemSelected(it)
                    navController.navigate(it) {
                        popUpTo(HomeScreenNavigation) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}
