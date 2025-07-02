package com.proyecto.ReUbica.data.api

import com.proyecto.ReUbica.data.model.producto.ProductoCreateResponse
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductoApiService {

    @Multipart
    @POST("productos/registrarProducto")
    suspend fun registrarProducto(
        @Header("Authorization") token: String,
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part product_image: MultipartBody.Part? = null
    ): Response<ProductoCreateResponse>

    @GET("productos/emprendimiento/{id}")
    suspend fun getProductosByEmprendimiento(
        @Header("Authorization") token: String,
        @Path("id") emprendimientoID: String
    ): Response<List<ProductoModel>>

}