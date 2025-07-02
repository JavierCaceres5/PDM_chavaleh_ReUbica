package com.proyecto.ReUbica.ui.screens.CartaProductos

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import com.proyecto.ReUbica.data.repository.ProductoRepository
import com.proyecto.ReUbica.data.local.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartaProductosViewModel(application: Application) : AndroidViewModel(application) {

    private val userSessionManager = UserSessionManager(application.applicationContext)
    private val productoRepository = ProductoRepository()

    private val _productos = MutableStateFlow<List<ProductoModel>>(emptyList())
    val productos = _productos.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun cargarProductos() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val token = userSessionManager.getToken()
                val emprendimientoID = userSessionManager.getEmprendimientoID()

                if (token == null) {
                    _error.value = "Token no encontrado"
                    return@launch
                }
                if (emprendimientoID == null) {
                    _error.value = "EmprendimientoID no encontrado"
                    return@launch
                }

                val response = productoRepository.getProductosByEmprendimiento("Bearer $token", emprendimientoID)
                if (response.isSuccessful) {
                    _productos.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar productos: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
}

