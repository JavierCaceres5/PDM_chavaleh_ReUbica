package com.proyecto.ReUbica.ui.screens.ComercioScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.RedesSociales
import com.proyecto.ReUbica.data.model.producto.ProductoModel
import com.proyecto.ReUbica.data.model.producto.ProductoResponse
import com.proyecto.ReUbica.data.model.producto.toProductoResponse
import com.proyecto.ReUbica.network.RetrofitInstance
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel

data class BusinessDetailState(
    val id: UUID = UUID.randomUUID(),
    val nombre: String? = null,
    val descripcion: String? = null,
    val categoriasSecundarias: List<String> = emptyList(),
    val categoriasPrincipales: List<String> = emptyList(),
    val logo: String? = null,
    val direccion: String? = null,
    val emprendimientoPhone: String? = null,
    val redes_sociales: RedesSociales? = null,
    val userID: UUID = UUID.randomUUID(),
    val latitud: Double? = null,
    val longitud: Double? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val location: LatLng = LatLng(0.0, 0.0),
    val products: List<ProductoResponse> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class ComercioViewModel : ViewModel() {

    private val _business = MutableStateFlow(BusinessDetailState())
    val business: StateFlow<BusinessDetailState> = _business

    fun setBusinessInfo(
        emprendimiento: EmprendimientoModel,
        userSessionManager: UserSessionManager
    ) {
        _business.update {
            it.copy(
                id = emprendimiento.id,
                nombre = emprendimiento.nombre,
                descripcion = emprendimiento.descripcion,
                categoriasSecundarias = emprendimiento.categoriasSecundarias,
                categoriasPrincipales = emprendimiento.categoriasPrincipales,
                logo = emprendimiento.logo,
                direccion = emprendimiento.direccion,
                emprendimientoPhone = emprendimiento.emprendimientoPhone,
                redes_sociales = emprendimiento.redes_sociales,
                userID = emprendimiento.userID,
                latitud = emprendimiento.latitud,
                longitud = emprendimiento.longitud,
                created_at = emprendimiento.created_at,
                updated_at = emprendimiento.updated_at,
                location = LatLng(
                    emprendimiento.latitud ?: 0.0,
                    emprendimiento.longitud ?: 0.0
                ),
                isLoading = true
            )
        }

        viewModelScope.launch {
            try {
                val token = userSessionManager.getToken()
                Log.d(
                    "ComercioViewModel",
                    "Iniciando llamada a getProductosByEmprendimiento con token: $token y ID: ${emprendimiento.id}"
                )

                val response = RetrofitInstance.productoApiService.getProductosByEmprendimiento(
                    token = "Bearer $token",
                    emprendimientoID = emprendimiento.id.toString()
                )

                if (response.isSuccessful) {
                    val productosModel = response.body() ?: emptyList<ProductoModel>()
                    val productosResponse: List<ProductoResponse> =
                        productosModel.map { it.toProductoResponse() }

                    _business.update { state ->
                        state.copy(
                            products = productosResponse,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    _business.update { state ->
                        state.copy(
                            isLoading = false,
                            error = "Error ${response.code()}: ${response.message()}"
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _business.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Error al cargar productos"
                    )
                }
            }
        }
    }
}
