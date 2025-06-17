package com.poyecto.ReUbica.ui.navigations

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poyecto.ReUbica.ui.layouts.CustomScaffold
import com.poyecto.ReUbica.ui.screens.FavoriteScreen
import com.poyecto.ReUbica.ui.screens.HomeScreen
import com.poyecto.ReUbica.ui.screens.ProfileScreen
import com.poyecto.ReUbica.ui.screens.SearchScreen
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
fun MainNavigation(){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WelcomeScreenNavigation
    ){
        composable<WelcomeScreenNavigation>{
            WelcomeScreen(navController)
        }

        composable<RegistroNavigation>{
            RegisterScreen(navController)
        }

        composable<LoginScreenNavigation>{
            LoginScreen(navController)
        }
        composable<mainNavigation> {
            CustomScaffold()
        }

    }
}