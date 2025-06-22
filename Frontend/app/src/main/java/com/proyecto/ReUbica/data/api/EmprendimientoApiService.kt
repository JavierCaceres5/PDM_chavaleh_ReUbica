package com.proyecto.ReUbica.data.api

import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoCreateRequest
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface EmprendimientoApiService {

    @GET("emprendimientos/categoria")
    suspend fun getEmprendimientosByCategoria(
        @Query("categoria") categoria: String
    ): Response<List<EmprendimientoModel>>

    @GET("emprendimientos/nombre")
    suspend fun getEmprendimientosByNombre(
        @Query("nombre") nombre: String
    ): Response<List<EmprendimientoModel>>

    @POST("emprendimientos/registrarEmprendimiento")
    suspend fun registrarEmprendimiento(
        @Body request: EmprendimientoCreateRequest
    ): Response<EmprendimientoResponse>

}
