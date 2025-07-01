package com.proyecto.ReUbica.data.repository

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoCreateRequest
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoResponse
import com.proyecto.ReUbica.data.model.producto.ProductoCreateRequest
import com.proyecto.ReUbica.data.model.producto.ProductoCreateResponse
import com.proyecto.ReUbica.network.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import androidx.core.net.toUri

class ProductoRepository {

    private val api = RetrofitInstance.productoApi

    suspend fun createProducto(
        context: Context,
        token: String,
        data: ProductoCreateRequest
    ): Response<ProductoCreateResponse> {

        fun String.toBody(): RequestBody =
            this.toRequestBody("text/plain".toMediaTypeOrNull())

        fun createMultipartFromUri(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri) ?: "image/*"
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val fileBytes = inputStream.readBytes()
            inputStream.close()

            val requestBody = fileBytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val fileName = uri.lastPathSegment ?: "image.jpg"

            return MultipartBody.Part.createFormData(partName, fileName, requestBody)
        }

        val productoImage = data.product_image?.let { imagePathOrUriString ->
            val uri = imagePathOrUriString.toUri()
            createMultipartFromUri(context, uri, "product_image")
        }

        return api.registrarProducto(
            token = "Bearer $token",
            nombre = data.nombre.toBody(),
            descripcion = data.descripcion.toBody(),
            precio = data.precio.toString().toBody(),
            product_image = productoImage
        )
    }

    suspend fun getProductosByEmprendimientoId(token: String, emprendimientoId: String): Response<List<ProductoCreateResponse>> {
        return api.getProductosByEmprendimientoId(token = "Bearer $token", emprendimientoId = emprendimientoId)
    }
}
