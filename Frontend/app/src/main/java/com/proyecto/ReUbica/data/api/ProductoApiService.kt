package com.proyecto.ReUbica.data.api

import com.proyecto.ReUbica.data.model.producto.ProductoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProductoApiService {

    @GET("productos/emprendimiento/{id}")
    suspend fun getProductosByEmprendimiento(
        @Header("Authorization") token: String,
        @Path("id") emprendimientoID: String
    ): Response<List<ProductoModel>>
}
