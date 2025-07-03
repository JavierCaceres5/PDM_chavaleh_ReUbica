package com.proyecto.ReUbica.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.ui.navigations.WelcomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.mainNavigation
import kotlinx.coroutines.flow.first

@Composable
fun SessionCheckScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val sessionManager = remember { UserSessionManager(application) }

    LaunchedEffect(Unit) {
        val session = sessionManager.userSessionFlow.first()
        if (session != null) {
            navController.navigate(mainNavigation) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate(WelcomeScreenNavigation) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF49724C))
    }
}
