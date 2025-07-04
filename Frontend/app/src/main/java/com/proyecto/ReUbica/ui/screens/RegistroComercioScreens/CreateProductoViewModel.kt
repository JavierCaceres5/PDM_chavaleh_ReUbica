package com.proyecto.ReUbica.ui.screens.RegistroComercioScreens

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.producto.ProductoCreateRequest
import com.proyecto.ReUbica.data.model.producto.ProductoCreateResponse
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import com.proyecto.ReUbica.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateProductoViewModel(
    private val userSessionManager: UserSessionManager,
    private val repository: ProductoRepository = ProductoRepository()
) : ViewModel() {

    private val _producto = MutableStateFlow(
        ProductoCreateRequest(
            nombre = "",
            descripcion = "",
            precio = 0.0,
            product_image = "",
        )
    )
    val producto = _producto.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success = _success.asStateFlow()

    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri = _imagenUri.asStateFlow()

    private val _productosExistentes = MutableStateFlow<List<ProductoModel>>(emptyList())
    val productosExistentes = _productosExistentes.asStateFlow()

    private val TAG = "CreateProductoViewModel"

    fun setProductosExistentes(lista: List<ProductoModel>) {
        _productosExistentes.value = lista
    }

    fun nombreProductoExiste(nombre: String): Boolean {
        return productosExistentes.value.any { it.nombre.equals(nombre.trim(), ignoreCase = true) }
    }

    fun setImage(uri: Uri?) {
        _imagenUri.value = uri
    }

    fun setValues(key: String, value: String) {
        _producto.value = when (key) {
            "nombre" -> _producto.value.copy(nombre = value)
            "descripcion" -> _producto.value.copy(descripcion = value)
            "precio" -> _producto.value.copy(precio = value.toDoubleOrNull() ?: 0.0)
            "product_image" -> _producto.value.copy(product_image = value)
            else -> _producto.value
        }
    }

    fun crearProducto(context: Context) {
        Log.d(TAG, "⚙️ Función crearProducto() invocada")

        viewModelScope.launch {
            val token = userSessionManager.getToken()
            Log.d(TAG, " Token obtenido: ${token ?: "Token nulo"}")

            if (token.isNullOrBlank()) {
                Log.e(TAG, " Error: Usuario no autenticado, token vacío o nulo")
                _error.value = "Usuario no autenticado"
                return@launch
            }

            val productoParaCrear = _producto.value
            Log.d(TAG, " Producto a crear: $productoParaCrear")

            _loading.value = true
            _error.value = null
            _success.value = false

            try {
                val response = repository.createProducto(context, token, productoParaCrear)
                Log.d(TAG, " Respuesta del servidor: $response")

                if (response.isSuccessful) {
                    val productoCreadoResponse = response.body()
                    if (productoCreadoResponse != null) {
                        userSessionManager.saveProductoID(productoCreadoResponse.producto.id.toString())
                        Log.d(TAG, " Producto creado exitosamente: ${productoCreadoResponse.producto}")
                        _success.value = true
                        clearProducto()
                    } else {
                        Log.e(TAG, " Respuesta exitosa pero cuerpo vacío (null)")
                        _error.value = "Respuesta vacía del servidor."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, " Error ${response.code()}: ${errorBody ?: "sin detalles"}")
                    _error.value = "Error ${response.code()}: ${errorBody ?: "sin detalles"}"
                }
            } catch (e: Exception) {
                Log.e(TAG, " Excepción inesperada al crear producto: ${e.localizedMessage}", e)
                _error.value = "Error inesperado: ${e.localizedMessage}"
            } finally {
                _loading.value = false
                Log.d(TAG, " Finalizó proceso de creación de producto")
            }
        }
    }

    fun clearProducto() {
        _producto.value = ProductoCreateRequest(
            nombre = "",
            descripcion = "",
            precio = 0.0,
            product_image = null,
        )
        _imagenUri.value = null
    }


    fun resetSuccess() {
        _success.value = false
    }

}
