package com.proyecto.ReUbica.data.model.producto

data class ProductoCreateRequest (
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val product_image: String? = null,
)