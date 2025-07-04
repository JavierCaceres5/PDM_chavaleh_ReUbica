package com.proyecto.ReUbica.ui.screens.RegistroComercioScreens

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoCreateRequest
import com.proyecto.ReUbica.data.model.emprendimiento.RedesSociales
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import com.proyecto.ReUbica.data.repository.EmprendimientoRepository
import com.proyecto.ReUbica.data.repository.ProductoRepository
import com.proyecto.ReUbica.ui.screens.ProfileScreen.ProfileScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class RegistroComercioViewModel : ViewModel() {

    private lateinit var userSessionManager: UserSessionManager
    private val repository = EmprendimientoRepository()
    private val productoRepository = ProductoRepository()

    private val _emprendimientoId = MutableStateFlow<UUID?>(null)
    val emprendimientoId = _emprendimientoId.asStateFlow()

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

    suspend fun obtenerProductosDelEmprendimiento(): List<ProductoModel> {
        if (!::userSessionManager.isInitialized) return emptyList()
        val token = userSessionManager.getToken() ?: return emptyList()
        val idEmprendimiento = _emprendimientoId.value ?: return emptyList()

        return try {
            val response = productoRepository.getProductosByEmprendimiento(token, idEmprendimiento.toString())
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun createEmprendimiento() {
        viewModelScope.launch {
            if (!::userSessionManager.isInitialized) {
                _error.value = "Session manager no inicializado"
                return@launch
            }

            val token = userSessionManager.getToken()

            if (token.isNullOrBlank()) {
                _error.value = "No se encontró token de sesión"
                return@launch
            }

            try {
                val response = repository.createEmprendimiento(token, _emprendimiento.value)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        userSessionManager.saveEmprendimientoID(body.emprendimiento.id.toString())
                        Log.d(TAG, "EmprendimientoID guardado: ${body.emprendimiento.id}")
                        
                        if (body.updatedToken.isNotBlank()) {
                            userSessionManager.actualizarSesionConNuevoToken(body.updatedToken)
                        }
                        _success.value = true
                        Log.d(TAG, "Emprendimiento creado exitosamente: ${_emprendimiento.value}")
                    } else {
                        _error.value = "Respuesta vacía del servidor"
                    }
                } else {
                    _error.value = "Error de creación de emprendimiento ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error en la creación: ${e.localizedMessage}"
            }
        }
    }


}