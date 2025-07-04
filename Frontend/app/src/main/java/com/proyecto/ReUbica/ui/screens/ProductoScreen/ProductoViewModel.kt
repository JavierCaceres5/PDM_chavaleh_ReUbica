package com.proyecto.ReUbica.ui.screens.ProductoScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import com.proyecto.ReUbica.data.repository.ProductoRepository
import com.proyecto.ReUbica.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductoRepository()

    private val _productos = MutableStateFlow<List<ProductoModel>>(emptyList())
    val productos: StateFlow<List<ProductoModel>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getProductosByEmprendimiento(token: String, emprendimientoID: String) {
        viewModelScope.launch {
            _loading.value = true

            try {
                val response = repository.getProductosByEmprendimiento(token, emprendimientoID)
                if (response.isSuccessful) {
                    _productos.value = response.body() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.message()}"
                    _productos.value = emptyList()
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
                _productos.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}
