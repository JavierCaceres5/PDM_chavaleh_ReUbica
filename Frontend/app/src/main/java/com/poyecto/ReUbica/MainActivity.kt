package com.proyecto.ReUbica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.proyecto.ReUbica.ui.navigations.Navigation
import com.proyecto.ReUbica.ui.theme.ReUbicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReUbicaTheme () {
                Navigation()
            }
        }
    }
}

