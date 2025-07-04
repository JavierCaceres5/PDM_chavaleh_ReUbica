package com.proyecto.ReUbica.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.CreateProductoViewModel

class CreateProductoViewModelFactory(
    private val userSessionManager: UserSessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateProductoViewModel(userSessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}