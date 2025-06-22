package com.proyecto.ReUbica.data.repository

import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoCreateRequest
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoResponse
import com.proyecto.ReUbica.network.RetrofitInstance
import retrofit2.Response

class EmprendimientoRepository {

    private val api = RetrofitInstance.emprendimientoApi

    suspend fun createEmprendimiento(request: EmprendimientoCreateRequest): Response<EmprendimientoResponse>{
        return api.registrarEmprendimiento(request)
    }

    suspend fun searchByName(nombre: String): Response<List<EmprendimientoModel>> {
        return api.getEmprendimientosByNombre(nombre)
    }

    suspend fun searchByCategory(categoria: String): Response<List<EmprendimientoModel>> {
        return api.getEmprendimientosByCategoria(categoria)
    }

}