package com.proyecto.ReUbica.data.api

import java.util.UUID

data class DummyProduct(
    val id: UUID,
    val name: String,
    val description: String,
    val price: Double,
    val rating: Float
)
