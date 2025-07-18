package com.proyecto.ReUbica.ui.screens.CartaProductos

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import com.proyecto.ReUbica.data.repository.ProductoRepository
import com.proyecto.ReUbica.data.repository.EmprendimientoRepository
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.producto.UpdateProductoRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartaProductosViewModel(application: Application) : AndroidViewModel(application) {

    private val userSessionManager = UserSessionManager(application.applicationContext)
    private val productoRepository = ProductoRepository()
    private val repository = EmprendimientoRepository()

    private val _productos = MutableStateFlow<List<ProductoModel>>(emptyList())
    val productos = _productos.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _eliminando = MutableStateFlow(false)
    val eliminando = _eliminando.asStateFlow()

    private val _actualizado = MutableStateFlow(false)
    val actualizado = _actualizado.asStateFlow()

    private val TAG = "CartaProductosViewModel"

    fun cargarProductos() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val token = userSessionManager.getToken()
                val emprendimientoID = repository.getMiEmprendimientoId(token.toString())
                Log.d(TAG, "Token: $token")
                Log.d(TAG, "EmprendimientoID: $emprendimientoID")

                if (token == null) {
                    _error.value = "Token no encontrado"
                    return@launch
                }
                if (emprendimientoID == null) {
                    _error.value = "EmprendimientoID no encontrado"
                    return@launch
                }
                val response = productoRepository.getProductosByEmprendimiento("$token", emprendimientoID)
                if (response.isSuccessful) {

                    val productosList = response.body() ?: emptyList()
                    _productos.value = productosList
                    productosList.forEach { producto ->
                        Log.d(TAG, "Producto ID: ${producto.id}")
                    }
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

    /*** CARGA INSTANTÁNEA AL ELIMINAR ***/
    fun eliminarProducto(productoID: String) {
        viewModelScope.launch {
            _eliminando.value = true
            _error.value = null

            try {
                val token = userSessionManager.getToken()
                if (token == null) {
                    _error.value = "Token no encontrado"
                    return@launch
                }
                val response = productoRepository.deleteProducto(token, productoID)
                if (response.isSuccessful) {
                    _productos.value = _productos.value.filterNot { it.id.toString() == productoID }
                } else {
                    _error.value = "Error al eliminar producto: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error inesperado: ${e.localizedMessage}"
            } finally {
                _eliminando.value = false
            }
        }
    }
    fun actualizarProducto(
        productoId: String,
        nombre: String,
        descripcion: String,
        precio: Double,
        nuevaImagenUri: Uri?
    ) {
        viewModelScope.launch {
            try {
                val token = userSessionManager.getToken() ?: return@launch
                val context = getApplication<Application>().applicationContext
                val response = productoRepository.updateProducto(
                    context, token, productoId, nombre, descripcion, precio, nuevaImagenUri
                )
                if (response.isSuccessful) {
                    cargarProductos()
                } else {
                    _error.value = "Error al actualizar producto: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.localizedMessage}"
            }
        }
    }

}

