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
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.UpdateEmprendimientoRequest
import com.proyecto.ReUbica.data.model.producto.DeleteProductoResponse
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import com.proyecto.ReUbica.data.model.producto.UpdateProductoRequest

class ProductoRepository {

    private val api = RetrofitInstance.productoApi

    suspend fun createProducto(
        context: Context,
        token: String,
        data: ProductoCreateRequest
    ): Response<ProductoCreateResponse> {

        fun String.toBody(): RequestBody =
            this.toRequestBody("text/plain".toMediaTypeOrNull())

        fun Double.toBody(): RequestBody =
            this.toString().toRequestBody("text/plain".toMediaTypeOrNull())

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

        val productoImagePart = data.product_image?.let { imagePathOrUriString ->
            val uri = imagePathOrUriString.toUri()
            createMultipartFromUri(context, uri, "product_image")
        }


        return api.registrarProducto(
            token = "Bearer $token",
            nombre = data.nombre.toBody(),
            descripcion = data.descripcion.toBody(),
            precio = data.precio.toBody(),
            product_image = productoImagePart
        )
    }

    suspend fun getProductosByEmprendimiento(token: String, emprendimientoID: String): Response<List<ProductoModel>> {
        return api.getProductosByEmprendimiento(token = "Bearer $token", emprendimientoID = emprendimientoID)
      }

    suspend fun deleteProducto(token: String, productoID: String): Response<DeleteProductoResponse>{
        return api.deleteProducto(token = "Bearer $token", productoID = productoID)
    }


    suspend fun updateProducto(
        context: Context,
        token: String,
        productoId: String,
        nombre: String,
        descripcion: String,
        precio: Double,
        nuevaImagenUri: Uri?
    ): Response<ProductoModel> {
        fun String.toBody(): RequestBody =
            this.toRequestBody("text/plain".toMediaTypeOrNull())
        fun Double.toBody(): RequestBody =
            this.toString().toRequestBody("text/plain".toMediaTypeOrNull())

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

        val imagenPart = nuevaImagenUri?.let { uri ->
            createMultipartFromUri(context, uri, "product_image")
        }

        return api.updateProducto(
            token = "Bearer $token",
            productoID = productoId,
            nombre = nombre.toBody(),
            descripcion = descripcion.toBody(),
            precio = precio.toBody(),
            product_image = imagenPart
        )
    }


    suspend fun updateProducto(token: String, productoId: String, updateData: UpdateProductoRequest): Response<Unit> {
        return api.updateProducto("Bearer $token", productoId, updateData)
    }

}
