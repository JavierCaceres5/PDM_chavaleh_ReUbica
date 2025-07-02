package com.proyecto.ReUbica.data.model.producto


data class ProductoResponse(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val product_image: String?,
    val created_at: String,
    val updated_at: String,
    val emprendimientoID: String
)