package com.proyecto.ReUbica.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductUpdateDTO(
    val name: String,
    val description: String,
    val price: Double,
    val image_url: String
)
