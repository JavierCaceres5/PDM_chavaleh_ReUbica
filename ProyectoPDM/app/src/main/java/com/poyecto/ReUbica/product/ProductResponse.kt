package com.proyecto.ReUbica.product

sealed interface ProductResponse {
    data class Success(val data: List<Product>? = null) : ProductResponse
    data class Error(val message: String?) : ProductResponse
}
