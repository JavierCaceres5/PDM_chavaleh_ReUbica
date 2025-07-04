package com.proyecto.ReUbica.data.model.review

import java.util.UUID

data class ReviewModel(
    val id: UUID,
    val created_at: String,
    val userID: UUID,
    val comentario: String,
    val rating: Double,
    val productoID: UUID
)
