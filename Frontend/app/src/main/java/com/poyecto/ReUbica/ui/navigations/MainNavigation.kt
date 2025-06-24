package com.poyecto.ReUbica.ui.navigations

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.poyecto.ReUbica.ui.layouts.CustomScaffold
import com.poyecto.ReUbica.ui.screens.DetallesComercioScreen
import com.poyecto.ReUbica.ui.screens.FavoriteScreen
import com.poyecto.ReUbica.ui.screens.HomeScreen
import com.poyecto.ReUbica.ui.screens.ProfileScreen
import com.poyecto.ReUbica.ui.screens.SearchScreen
import com.proyecto.ReUbica.ui.navigations.DetallesComercioNavigation
import com.proyecto.ReUbica.ui.navigations.FavoritesScreenNavigation
import com.proyecto.ReUbica.ui.navigations.HomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.LoginScreenNavigation
import com.proyecto.ReUbica.ui.navigations.ProfileScreenNavigation
import com.proyecto.ReUbica.ui.navigations.RegistroNavigation
import com.proyecto.ReUbica.ui.navigations.SearchScreenNavigation
import com.proyecto.ReUbica.ui.navigations.WelcomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.mainNavigation
import com.proyecto.ReUbica.ui.screens.LoginScreen
import com.proyecto.ReUbica.ui.screens.RegisterScreen
import com.proyecto.ReUbica.ui.screens.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController() // ✅ Esto es el ÚNICO navController

    NavHost(
        navController = navController,
        startDestination = WelcomeScreenNavigation
    ) {
        composable<WelcomeScreenNavigation> {
            WelcomeScreen(navController)
        }

        composable<RegistroNavigation> {
            RegisterScreen(navController)
        }

        composable<LoginScreenNavigation> {
            LoginScreen(navController)
        }

        composable("mainNavigation") {
            var selectedItem by rememberSaveable { mutableStateOf("nowplaying") }

            CustomScaffold(
                navController = navController,
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                content = { HomeScreen(navController = navController)
                }
            )
        }

        composable(
            route = "DetallesComercioScreen/{nombre}/{departamento}/{categoria}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("departamento") { type = NavType.StringType },
                navArgument("categoria") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val departamento = backStackEntry.arguments?.getString("departamento") ?: ""
            val categoria = backStackEntry.arguments?.getString("categoria") ?: ""

            DetallesComercioScreen(
                navController = navController,
                nombre = nombre,
                departamento = departamento,
                categoria = categoria
            )
        }

        composable("FavoriteScreen") {
            FavoriteScreen(navController = navController)
        }
    }
}
