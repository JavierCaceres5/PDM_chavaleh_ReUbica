package com.proyecto.ReUbica.data.repository

import com.proyecto.ReUbica.data.api.ProductoApiService
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import retrofit2.Response

class ProductoRepository(private val api: ProductoApiService) {

    suspend fun getProductosByEmprendimiento(
        token: String,
        emprendimientoID: String
    ): Response<List<ProductoModel>> {
        return api.getProductosByEmprendimiento(
            token = "Bearer $token",
            emprendimientoID = emprendimientoID
        )
    }
}
