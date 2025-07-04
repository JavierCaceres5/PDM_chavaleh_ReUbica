package com.proyecto.ReUbica.data.api

import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoCreateRequest
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoDeleteResponse
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoResponse
import com.proyecto.ReUbica.data.model.emprendimiento.UpdateEmprendimientoRequest
import com.proyecto.ReUbica.data.model.user.UpdateProfileRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface EmprendimientoApiService {

    @GET("emprendimientos/categoria")
    suspend fun getEmprendimientosByCategoria(
        @Query("categoria") categoria: String
    ): Response<List<EmprendimientoModel>>


    @Multipart
    @POST("emprendimientos/registrarEmprendimiento")
    suspend fun registrarEmprendimiento(
        @Header("Authorization") token: String,
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("categoriasPrincipales") categoriasPrincipales: RequestBody,
        @Part("categoriasSecundarias") categoriasSecundarias: RequestBody,
        @Part("direccion") direccion: RequestBody,
        @Part("emprendimientoPhone") phone: RequestBody,
        @Part("redes_sociales") redes: RequestBody,
        @Part("latitud") lat: RequestBody,
        @Part("longitud") lng: RequestBody,
        @Part logo: MultipartBody.Part? = null
    ): Response<EmprendimientoResponse>

    @DELETE("emprendimientos/eliminarMiEmprendimiento")
    suspend fun deleteMiEmprendimiento(
        @Header("Authorization") token: String
    ): Response<EmprendimientoDeleteResponse>

    @GET("emprendimientos/nombre")
    suspend fun getEmprendimientosByNombre(
        @Header("Authorization") token: String,
        @Query("nombre") nombre: String
    ): Response<List<EmprendimientoModel>>


    @GET("emprendimientos/miEmprendimiento")
    suspend fun getMiEmprendimiento(
        @Header("Authorization") token: String
    ): Response<EmprendimientoModel>


    @PUT("emprendimientos/actualizarMiEmprendimiento")
    suspend fun updateEmprendimiento(
        @Header("Authorization") token: String,
        @Body updateData: UpdateEmprendimientoRequest
    ): Response<Unit>

    @GET("emprendimientos/")
    suspend fun getAllEmprendimientos(
        @Header("Authorization") token: String
    ): Response<List<EmprendimientoModel>>

    @Multipart
    @PUT("emprendimientos/actualizarMiEmprendimiento")
    suspend fun updateEmprendimientoLogo(
        @Header("Authorization") token: String,
        @Part logo: MultipartBody.Part
    ): Response<Any>

}