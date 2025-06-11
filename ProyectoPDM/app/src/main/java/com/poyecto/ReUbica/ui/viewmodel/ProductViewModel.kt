package com.proyecto.ReUbica.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.product.Product
import com.proyecto.ReUbica.product.ProductManager
import com.proyecto.ReUbica.product.ProductResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProductViewModel : ViewModel() {

    private val productManager = ProductManager()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val TAG = "SUPABASE"

    fun fetchProducts() {
        viewModelScope.launch {
            productManager.getAllProducts().collect { response ->
                when (response) {
                    is ProductResponse.Success -> {
                        val count = response.data?.size ?: 0
                        Log.d(TAG, "Productos cargados: $count")
                        _products.value = response.data ?: emptyList()
                        _error.value = null
                    }
                    is ProductResponse.Error -> {
                        Log.e(TAG, "Error al cargar productos: ${response.message}")
                        _error.value = response.message ?: "Error desconocido"
                    }
                }
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            productManager.addProduct(product).collect { response ->
                when (response) {
                    is ProductResponse.Success -> Log.d(TAG, "Producto insertado correctamente")
                    is ProductResponse.Error -> Log.e(TAG, "Error al insertar producto: ${response.message}")
                }
                fetchProducts()
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productManager.updateProduct(product).collect { response ->
                when (response) {
                    is ProductResponse.Success -> Log.d(TAG, "Producto actualizado")
                    is ProductResponse.Error -> Log.e(TAG, "Error al actualizar producto: ${response.message}")
                }
                fetchProducts()
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            productManager.deleteProduct(productId).collect { response ->
                when (response) {
                    is ProductResponse.Success -> Log.d(TAG, "Producto eliminado")
                    is ProductResponse.Error -> Log.e(TAG, "Error al eliminar producto: ${response.message}")
                }
                fetchProducts()
            }
        }
    }
}
