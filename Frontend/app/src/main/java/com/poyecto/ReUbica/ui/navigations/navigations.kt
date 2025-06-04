package com.proyecto.ReUbica.ui.navigations

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyecto.ReUbica.ui.screens.LoginScreen
import com.proyecto.ReUbica.ui.screens.RegisterScreen
import com.proyecto.ReUbica.ui.screens.WelcomeScreen
import kotlinx.serialization.Serializable

@Serializable
object WelcomeScreenNavigation

@Serializable
object RegistroNavigation

@Serializable
object LoginScreenNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WelcomeScreenNavigation::class.qualifiedName!!
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

    }

}