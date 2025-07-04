package com.proyecto.ReUbica.ui.screens.ProfileScreen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Tag
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.model.user.UserProfile
import com.proyecto.ReUbica.data.repository.EmprendimientoRepository
import com.proyecto.ReUbica.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileScreenViewModel(application: Application): AndroidViewModel(application) {

    private val repository = UserRepository()
    private val emprendimientoRepository = EmprendimientoRepository()
    private val sessionManager = UserSessionManager(application)

    private val _emprendimiento = MutableStateFlow<EmprendimientoModel?>(null)
    val emprendimiento: StateFlow<EmprendimientoModel?> = _emprendimiento

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val TAG = "ProfileScreenViewModel"

    val userSession = sessionManager.userSessionFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    init {
        refreshUserData()
    }

    fun refreshUserData() {
        viewModelScope.launch {
            try {
                val token = sessionManager.getToken()
                if (token == null) {
                    _error.value = "Token no encontrado"
                    return@launch
                }

                // Solo lee el usuario actual de la sesión
                val userProfile = sessionManager.getUserProfile(token)
                if (userProfile != null) {
                    Log.d(TAG, "Datos de usuario obtenidos localmente: ${userProfile.user_role}")
                } else {
                    Log.e("ProfileVM", "Perfil no encontrado en almacenamiento local")
                    _error.value = "Perfil no encontrado en almacenamiento local"
                }
            } catch (e: Exception) {
                Log.e("ProfileVM", "Error actualizando datos: ${e.message}")
                _error.value = "Error al actualizar datos: ${e.message}"
            }
        }
    }

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

    fun deleteMiEmprendimiento(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val token = sessionManager.getToken()
                if (token.isNullOrBlank()) {
                    _error.value = "Token no encontrado. Por favor, inicie sesión nuevamente."
                    _loading.value = false
                    return@launch
                }

                val response = emprendimientoRepository.deleteMiEmprendimiento(token)
                if (response.isSuccessful) {
                    Log.d(TAG, "Emprendimiento eliminado correctamente")
                    refreshUserData()
                    _loading.value = false
                    onSuccess()
                } else {
                    _error.value = "Error al eliminar emprendimiento: ${response.message()}"
                    Log.e(TAG, "Error eliminando emprendimiento: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
                Log.e(TAG, "Error de red: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

}
