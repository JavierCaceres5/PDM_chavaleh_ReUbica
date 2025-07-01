package com.proyecto.ReUbica.data.model.producto

import java.util.UUID

data class ProductoModel(
    val id: UUID,
    val nombre: String?,
    val descripcion: String?,
    val precio: Double?,
    val product_image: String?,
    val created_at: String?,
    val updated_at: String?,
    val emprendimientoID: UUID,
)