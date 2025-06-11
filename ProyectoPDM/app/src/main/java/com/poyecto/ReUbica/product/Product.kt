package com.proyecto.ReUbica.product

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val image_url: String,
    val created_at: String? = null
)
