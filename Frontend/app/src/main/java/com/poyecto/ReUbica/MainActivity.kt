package com.proyecto.ReUbica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.poyecto.ReUbica.ui.navigations.MainNavigation
import com.proyecto.ReUbica.ui.theme.ReUbicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReUbicaTheme () {
                MainNavigation()
            }
        }
    }
}

