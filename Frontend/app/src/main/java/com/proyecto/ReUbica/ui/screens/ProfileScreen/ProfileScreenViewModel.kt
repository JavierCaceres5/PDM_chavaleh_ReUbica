package com.proyecto.ReUbica.ui.screens.ProfileScreen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.model.user.UserProfile
import com.proyecto.ReUbica.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileScreenViewModel(application: Application): AndroidViewModel(application) {

    private val repository = UserRepository()
    private val sessionManager = UserSessionManager(application)

    private val _emprendimiento = MutableStateFlow<EmprendimientoModel?>(null)
    val emprendimiento: StateFlow<EmprendimientoModel?> = _emprendimiento

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val token = sessionManager.getToken()
                if (token == null) {
                    _error.value = "Token no encontrado. Por favor, inicie sesión nuevamente."
                    _loading.value = false
                    return@launch
                }
                val response = repository.deleteAccount(token)
                if (response.isSuccessful) {
                    sessionManager.clearSession()
                    onSuccess()
                } else {
                    _error.value = "Error al eliminar cuenta: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

}