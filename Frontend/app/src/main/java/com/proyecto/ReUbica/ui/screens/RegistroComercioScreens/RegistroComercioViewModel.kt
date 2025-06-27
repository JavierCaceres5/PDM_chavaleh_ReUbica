package com.proyecto.ReUbica.ui.screens.RegistroComercioScreens

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoCreateRequest
import com.proyecto.ReUbica.data.model.emprendimiento.RedesSociales
import com.proyecto.ReUbica.data.repository.EmprendimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroComercioViewModel : ViewModel() {

    private lateinit var userSessionManager: UserSessionManager
    private val repository = EmprendimientoRepository()

    private val _emprendimiento = MutableStateFlow(
        EmprendimientoCreateRequest(
            nombre = "",
            descripcion = "",
            categoriasPrincipales = listOf(),
            categoriasSecundarias = listOf(),
            logo = null,
            direccion = "",
            redes_sociales = RedesSociales(),
            latitud = 0.0,
            emprendimientoPhone = "",
            longitud = 0.0
        )
    )
    val emprendimiento = _emprendimiento.asStateFlow()

    private val _redesSociales = MutableStateFlow(RedesSociales())
    val redesSociales = _redesSociales.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success = _success.asStateFlow()

    private val TAG = "RegistroComercioViewModel"

    fun initSessionManager(context: Context) {
        if (!::userSessionManager.isInitialized) {
            userSessionManager = UserSessionManager(context.applicationContext)
        }
    }

    fun setValues(key: String, value: String) {
        _emprendimiento.value = when (key) {
            "nombre" -> _emprendimiento.value.copy(nombre = value)
            "descripcion" -> _emprendimiento.value.copy(descripcion = value)
            "direccion" -> _emprendimiento.value.copy(direccion = value)
            "telefono" -> _emprendimiento.value.copy(emprendimientoPhone = value)
            "logo" -> _emprendimiento.value.copy(logo = value)
            "latitud" -> _emprendimiento.value.copy(latitud = value.toDoubleOrNull() ?: 0.0)
            "longitud" -> _emprendimiento.value.copy(longitud = value.toDoubleOrNull() ?: 0.0)
            else -> _emprendimiento.value
        }
    }

    fun setCategoriasPrincipales(lista: List<String>) {
        _emprendimiento.value = _emprendimiento.value.copy(categoriasPrincipales = lista)
    }

    fun setCategoriasSecundarias(lista: List<String>) {
        _emprendimiento.value = _emprendimiento.value.copy(categoriasSecundarias = lista)
    }

    fun setRedesSociales(key: String, value: String) {
        val valor = if (value.isBlank()) null else value
        val redesActuales = _emprendimiento.value.redes_sociales
        val nuevasRedes = when (key) {
            "Facebook" -> redesActuales.copy(Facebook = valor)
            "Instagram" -> redesActuales.copy(Instagram = valor)
            "TikTok" -> redesActuales.copy(TikTok = valor)
            "Twitter" -> redesActuales.copy(Twitter = valor)
            else -> redesActuales
        }
        _emprendimiento.value = _emprendimiento.value.copy(redes_sociales = nuevasRedes)
    }

    fun createEmprendimiento() {
        viewModelScope.launch {
            if (!::userSessionManager.isInitialized) {
                _error.value = "Session manager no inicializado"
                return@launch
            }
            _loading.value = true
            val token = userSessionManager.getToken()
            if (token.isNullOrBlank()) {
                _error.value = "No se encontró token de sesión"
                _loading.value = false
                return@launch
            }
            val response = repository.createEmprendimiento(token, _emprendimiento.value)
            Log.e(TAG, response.toString())
            if (!response.isSuccessful) {
                _error.value = "Error de creación de emprendimiento: ${response.message()}"
                _loading.value = false
                Log.e(TAG, "Error de emprendimiento: ${_emprendimiento.value}")
                Log.e(TAG, token)
                return@launch
            }
            Log.e(TAG, "Emprendimiento creado exitosamente: ${_emprendimiento.value}")
            _error.value = null
            _success.value = true
            _loading.value = false
        }
    }
}
