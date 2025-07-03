package com.proyecto.ReUbica.ui.navigations

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.proyecto.ReUbica.ui.layouts.CustomScaffold
import com.proyecto.ReUbica.ui.screens.LoginScreen.LoginScreen
import com.proyecto.ReUbica.ui.screens.ProductoScreen.ProductDetailScreen
import com.proyecto.ReUbica.ui.screens.RegisterScreen.RegisterScreen
import com.proyecto.ReUbica.ui.screens.ResetPassword.ResetPasswordScreen
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

        composable<ResetPasswordScreenNavigation> {
            ResetPasswordScreen(navController)
        }

        composable(
            route = "product_detail/{productId}?token={token}&emprendimientoID={emprendimientoID}",
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType },
                navArgument("emprendimientoID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val emprendimientoID = backStackEntry.arguments?.getString("emprendimientoID") ?: ""

            ProductDetailScreen(
                productId = productId,
                token = token,
                emprendimientoID = emprendimientoID,
                navController = navController
            )
        }

    }
}
