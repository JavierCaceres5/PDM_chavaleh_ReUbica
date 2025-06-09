package com.poyecto.ReUbica.ui.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poyecto.ReUbica.ui.navigations.MainNavigation
import com.poyecto.ReUbica.ui.screens.FavoriteScreen
import com.poyecto.ReUbica.ui.screens.HomeScreen
import com.poyecto.ReUbica.ui.screens.ProfileScreen
import com.poyecto.ReUbica.ui.screens.SearchScreen
import com.proyecto.ReUbica.ui.navigations.FavoritesScreenNavigation
import com.proyecto.ReUbica.ui.navigations.HomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.ProfileScreenNavigation
import com.proyecto.ReUbica.ui.navigations.SearchScreenNavigation

data class navItem(
    val title: String,
    val icon: ImageVector,
    val route: Any
)

@Composable
fun CustomScaffold(){

    val navController = rememberNavController()
    var title by rememberSaveable { mutableStateOf("Home") }
    var selectedItem by rememberSaveable { mutableStateOf("nowplaying") }

    val navItems = listOf(
        navItem("Inicio", Icons.Filled.Home, "nowplaying"),
        navItem("Buscar", Icons.Default.Search, "search"),
        navItem("Mi cuenta", Icons.Filled.AccountCircle, "account"),
        navItem("Favoritos", Icons.Default.FavoriteBorder, "favorites")
    )

    fun onItemSelected(currentItem: String) {
        selectedItem = currentItem
        title = when (currentItem) {
            "nowplaying" -> "Inicio"
            "search" -> "Buscar"
            "account" -> "Mi cuenta"
            "favorites" -> "Favoritos"
            else -> "Inicio"
        }
        when (currentItem) {
            "nowplaying" -> navController.navigate(HomeScreenNavigation)
            "search" -> navController.navigate(SearchScreenNavigation)
            "account" -> navController.navigate(ProfileScreenNavigation)
            "favorites" -> navController.navigate(FavoritesScreenNavigation)
            else -> navController.navigate(HomeScreenNavigation)
        }
    }


    Scaffold (
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar( navItems = navItems,
            selectedItem = selectedItem,
            onItemSelected = { onItemSelected(it) }) },
        containerColor = Color.White
    ) { innerPadding ->
        Column {

            NavHost(

                navController = navController,
                startDestination = HomeScreenNavigation,
                Modifier.padding(innerPadding)

            ) {
                composable<HomeScreenNavigation>{
                    HomeScreen(navController = navController)
                }

                composable<FavoritesScreenNavigation>{
                    FavoriteScreen(navController)
                }

                composable<SearchScreenNavigation>{
                    SearchScreen(navController)
                }

                composable<ProfileScreenNavigation>{
                    ProfileScreen(navController)
                }
            }

            Spacer(modifier = Modifier.padding(innerPadding))
        }
    }

}