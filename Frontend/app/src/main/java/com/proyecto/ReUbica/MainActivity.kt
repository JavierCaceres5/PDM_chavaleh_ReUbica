package com.proyecto.ReUbica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.libraries.places.api.Places
import com.proyecto.ReUbica.ui.navigations.MainNavigation
import com.proyecto.ReUbica.ui.theme.ReUbicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyAz8fzjMNnhHgoIHZuxrhIHpNo2brGAqBI")
        }
        enableEdgeToEdge()
        setContent {
            ReUbicaTheme () {
                MainNavigation()
            }
        }
    }
}
