package com.proyecto.ReUbica.data.api

import com.proyecto.ReUbica.data.model.review.CreateReviewRequest
import com.proyecto.ReUbica.data.model.review.EmprendimientoReviewsResponse
import com.proyecto.ReUbica.data.model.review.ReviewModelResponse
import retrofit2.Response
import retrofit2.http.*

interface ReviewApiService {

    // Obtener todas las valoraciones de un emprendimiento
    @GET("productos/emprendimiento/{id}/ratings")
    suspend fun getReviewsByEmprendimiento(
        @Header("Authorization") token: String,
        @Path("id") emprendimientoID: String
    ): Response<List<EmprendimientoReviewsResponse>>

    // Publicar una valoración para un producto
    @POST("productos/{productoID}/ratings")
    suspend fun postReview(
        @Header("Authorization") token: String,
        @Path("productoID") productoID: String,
        @Body reviewRequest: CreateReviewRequest
    ): Response<ReviewModelResponse>

    // Eliminar la valoración de un usuario para un producto
    @DELETE("productos/{productoID}/ratings")
    suspend fun deleteReview(
        @Header("Authorization") token: String,
        @Path("productoID") productoID: String
    ): Response<Unit>
}
