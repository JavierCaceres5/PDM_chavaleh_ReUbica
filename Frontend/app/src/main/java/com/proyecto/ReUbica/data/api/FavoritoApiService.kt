package com.proyecto.ReUbica.data.api

import com.proyecto.ReUbica.data.model.favorito.FavoritoResponse
import retrofit2.Response
import retrofit2.http.*

data class ToggleFavoritoRequest(
    val tipo_objetivo: String,
    val productoID: String? = null,
    val emprendimientoID: String? = null
)

interface FavoritoApiService {
    @POST("favoritos/toggle")
    suspend fun toggleFavorito(
        @Header("Authorization") token: String,
        @Body body: ToggleFavoritoRequest
    ): Response<Unit>

    @GET("favoritos")
    suspend fun getFavoritos(
        @Header("Authorization") token: String,
        @Query("tipo_objetivo") tipo: String
    ): Response<List<FavoritoResponse>>


}
