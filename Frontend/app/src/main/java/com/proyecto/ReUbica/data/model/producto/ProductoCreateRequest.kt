package com.proyecto.ReUbica.data.model.producto

import java.util.UUID

data class ProductoCreateRequest (
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val product_image: String? = null,
)