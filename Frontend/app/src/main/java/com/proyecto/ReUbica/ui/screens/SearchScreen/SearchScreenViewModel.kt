package com.proyecto.ReUbica.ui.screens.EmprendimientosBuscar

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.repository.EmprendimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EmprendimientoRepository()
    private lateinit var userSessionManager: UserSessionManager

    fun setUserSessionManager(manager: UserSessionManager) {
        userSessionManager = manager
    }

    private val _resultadosByNombre = MutableStateFlow<List<EmprendimientoModel>>(emptyList())
    val resultadosByNombre: StateFlow<List<EmprendimientoModel>> = _resultadosByNombre

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun searchEmprendimientoByNombre(nombre: String) {

        viewModelScope.launch {
            _loading.value = true
            try {
                val token = userSessionManager.getToken()

                if (token.isNullOrBlank()) {
                    _error.value = "Token no disponible"
                    _loading.value = false
                    return@launch
                }

                val response = repository.searchByName(token, nombre)
                if (response.isSuccessful) {
                    _resultadosByNombre.value = response.body() ?: emptyList()
                    _error.value = null
                } else {
                    Log.e("BuscarViewModel", "Código: ${response.code()}, Mensaje: ${response.message()}")
                    _error.value = "Error ${response.code()}: ${response.message()}"
                    _resultadosByNombre.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("BuscarViewModel", "Error de red: ${e.message}")
                _error.value = "Ocurrió un error de red."
                _resultadosByNombre.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}
