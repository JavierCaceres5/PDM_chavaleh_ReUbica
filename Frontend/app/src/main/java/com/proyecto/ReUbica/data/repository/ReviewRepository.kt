package com.proyecto.ReUbica.data.repository

import com.proyecto.ReUbica.data.model.review.CreateReviewRequest
import com.proyecto.ReUbica.data.model.review.EmprendimientoReviewsResponse
import com.proyecto.ReUbica.data.model.review.ReviewModel
import com.proyecto.ReUbica.data.model.review.ReviewModelResponse
import com.proyecto.ReUbica.data.api.ReviewApiService
import retrofit2.Response

class ReviewRepository(private val api: ReviewApiService) {

    suspend fun postReview(token: String, productoID: String, comentario: String, rating: Double): Response<ReviewModelResponse> {
        val request = CreateReviewRequest(comentario, rating)
        return api.postReview(token, productoID, request)
    }

    suspend fun deleteReview(token: String, productoID: String): Response<Unit> {
        return api.deleteReview(token, productoID)
    }

    suspend fun getReviewsByEmprendimiento(token: String, emprendimientoID: String): Response<List<EmprendimientoReviewsResponse>> {
        return api.getReviewsByEmprendimiento(token, emprendimientoID)
    }
}
