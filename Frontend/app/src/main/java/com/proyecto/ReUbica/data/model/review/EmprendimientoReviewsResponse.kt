package com.proyecto.ReUbica.data.model.review

data class EmprendimientoReviewsResponse(
    val productoID: String,
    val nombre: String,
    val valoraciones: List<ReviewModel>
)
