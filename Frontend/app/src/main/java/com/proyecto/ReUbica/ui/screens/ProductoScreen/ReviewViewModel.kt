package com.proyecto.ReUbica.ui.screens.ProductoScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.model.review.EmprendimientoReviewsResponse
import com.proyecto.ReUbica.data.repository.ReviewRepository
import com.proyecto.ReUbica.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReviewRepository(RetrofitInstance.reviewApiService)

    private val _emprendimientoReviews = MutableStateFlow<List<EmprendimientoReviewsResponse>>(emptyList())
    val emprendimientoReviews: StateFlow<List<EmprendimientoReviewsResponse>> = _emprendimientoReviews

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Obtiene todas las valoraciones de un emprendimiento
     * y luego se filtran en el frontend por productoID.
     */
    fun getReviewsByEmprendimiento(token: String, emprendimientoID: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"

                val response = repository.getReviewsByEmprendimiento(bearerToken, emprendimientoID)
                if (response.isSuccessful) {
                    _emprendimientoReviews.value = response.body() ?: emptyList()
                    _error.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error ${response.code()}: ${response.message()}"
                    Log.e(
                        "ReviewError",
                        "Código: ${response.code()}, Mensaje: ${response.message()}, Body: $errorBody"
                    )
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
                Log.e("ReviewError", "Excepción: ${e.localizedMessage}", e)
            } finally {
                _loading.value = false
            }
        }
    }


    /**
     * Publica una reseña para un producto y refresca las valoraciones tras subirla.
     */
    fun postReview(token: String, productoID: String, comentario: String, rating: Double, emprendimientoID: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.postReview(token, productoID, comentario, rating)
                if (response.isSuccessful) {
                    getReviewsByEmprendimiento(token, emprendimientoID)
                    _error.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error al publicar: ${response.code()} - ${response.message()}"
                    Log.e("ReviewError", "Código: ${response.code()}, Mensaje: ${response.message()}, Body: $errorBody")
                }
            } catch (e: Exception) {
                _error.value = "Error al publicar: ${e.localizedMessage}"
                Log.e("ReviewError", "Excepción: ${e.localizedMessage}", e)
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Elimina la reseña del usuario actual y refresca las valoraciones tras borrarla.
     */
    fun deleteReview(token: String, productoID: String, emprendimientoID: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.deleteReview(token, productoID)
                if (response.isSuccessful) {
                    getReviewsByEmprendimiento(token, emprendimientoID)
                    _error.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error al eliminar: ${response.code()} - ${response.message()}"
                    Log.e("ReviewError", "Código: ${response.code()}, Mensaje: ${response.message()}, Body: $errorBody")
                }
            } catch (e: Exception) {
                _error.value = "Error al eliminar: ${e.localizedMessage}"
                Log.e("ReviewError", "Excepción: ${e.localizedMessage}", e)
            } finally {
                _loading.value = false
            }
        }
    }
}
