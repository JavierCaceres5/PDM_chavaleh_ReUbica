package com.proyecto.ReUbica.data.repository

import com.google.gson.Gson
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoCreateRequest
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoDeleteResponse
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoResponse
import com.proyecto.ReUbica.data.model.emprendimiento.UpdateEmprendimientoRequest
import com.proyecto.ReUbica.network.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class EmprendimientoRepository {

    private val api = RetrofitInstance.emprendimientoApi

    suspend fun createEmprendimiento(token: String, data: EmprendimientoCreateRequest): Response<EmprendimientoResponse> {
        val gson = Gson()

        fun String.toBody(): RequestBody =
            this.toRequestBody("text/plain".toMediaTypeOrNull())

        val logoPart = data.logo?.let { path ->
            val file = File(path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("logo", file.name, requestFile)
        }

        return api.registrarEmprendimiento(
            token = "Bearer $token",
            nombre = data.nombre.toBody(),
            descripcion = data.descripcion.toBody(),
            categoriasPrincipales = gson.toJson(data.categoriasPrincipales).toBody(),
            categoriasSecundarias = gson.toJson(data.categoriasSecundarias).toBody(),
            direccion = data.direccion.toBody(),
            phone = data.emprendimientoPhone.toBody(),
            redes = gson.toJson(data.redes_sociales).toBody(),
            lat = data.latitud.toString().toBody(),
            lng = data.longitud.toString().toBody(),
            logo = logoPart
        )
    }

    suspend fun searchByName(token: String, nombre: String): Response<List<EmprendimientoModel>> {
        return api.getEmprendimientosByNombre("Bearer $token", nombre)
    }

    suspend fun searchByCategory(categoria: String): Response<List<EmprendimientoModel>> {
        return api.getEmprendimientosByCategoria(categoria)
    }

    suspend fun deleteMiEmprendimiento(token: String): Response<EmprendimientoDeleteResponse> {
        return api.deleteMiEmprendimiento("Bearer $token")
    }

    suspend fun getMiEmprendimiento(token: String): Response<EmprendimientoModel> {
        return api.getMiEmprendimiento("Bearer $token")
    }

    suspend fun getMiEmprendimientoId(token: String): String? {
        return try {
            val response = getMiEmprendimiento(token)
            if (response.isSuccessful) {
                response.body()?.id?.toString()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateEmprendimiento(token: String, updateData: UpdateEmprendimientoRequest): Response<Unit> {
        return api.updateEmprendimiento("Bearer $token", updateData)
    }

    suspend fun getAllEmprendimientos(token: String): Response<List<EmprendimientoModel>> {
        return api.getAllEmprendimientos("Bearer $token")
    }

    suspend fun updateEmprendimientoLogo(token: String, filePath: String): Response<Any> {
        val file = File(filePath)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val logo = MultipartBody.Part.createFormData("logo", file.name, requestFile)
        return api.updateEmprendimientoLogo("Bearer $token", logo)
    }

}
