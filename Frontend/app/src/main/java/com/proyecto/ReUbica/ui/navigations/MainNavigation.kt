package com.proyecto.ReUbica.ui.navigations

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyecto.ReUbica.ui.layouts.CustomScaffold
import com.proyecto.ReUbica.ui.screens.ComercioScreen.ProductDetailScreen
import com.proyecto.ReUbica.ui.screens.LoginScreen.LoginScreen
import com.proyecto.ReUbica.ui.screens.RegisterScreen.RegisterScreen
import com.proyecto.ReUbica.ui.screens.SessionCheckScreen
import com.proyecto.ReUbica.ui.screens.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SessionCheckNavigation
    ) {
        composable<SessionCheckNavigation> {
            SessionCheckScreen(navController)
        }
        composable<WelcomeScreenNavigation> {
            WelcomeScreen(navController)
        }
        composable<RegistroNavigation> {
            RegisterScreen(navController)
        }
        composable<LoginScreenNavigation> {
            LoginScreen(navController)
        }
        composable<mainNavigation> {
            CustomScaffold(navController)
        }
    }
}
