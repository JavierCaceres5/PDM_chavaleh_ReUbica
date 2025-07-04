package com.proyecto.ReUbica.data.repository

import com.proyecto.ReUbica.data.api.ToggleFavoritoRequest
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.model.favorito.FavoritoResponse
import com.proyecto.ReUbica.network.RetrofitInstance

object FavoritoRepository {

    suspend fun toggleFavorito(token: String, tipo: String, objetivoId: String): Boolean {
        val authHeader = "Bearer $token"
        val body = when (tipo) {
            "producto" -> ToggleFavoritoRequest(tipo_objetivo = "producto", productoID = objetivoId)
            else -> ToggleFavoritoRequest(
                tipo_objetivo = "emprendimiento",
                emprendimientoID = objetivoId
            )
        }

        return try {
            val response = RetrofitInstance.favoritoApi.toggleFavorito(authHeader, body)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
    suspend fun getEmprendimientosFavoritos(token: String): List<EmprendimientoModel> {
        val authHeader = "Bearer $token"
        return try {
            val response = RetrofitInstance.favoritoApi.getFavoritos(authHeader, "emprendimiento")
            if (response.isSuccessful) {
                val favoritos = response.body() ?: emptyList()
                favoritos.mapNotNull { it.Comercio }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getFavoritos(token: String, tipo: String): List<String> {
        val authHeader = "Bearer $token"
        return try {
            val response = RetrofitInstance.favoritoApi.getFavoritos(authHeader, tipo)
            if (response.isSuccessful) {
                val favoritos = response.body() ?: emptyList()
                favoritos.mapNotNull {
                    when (tipo) {
                        "producto" -> it.Comercio?.id?.toString()
                        else -> it.Comercio?.id?.toString()
                    }
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


}