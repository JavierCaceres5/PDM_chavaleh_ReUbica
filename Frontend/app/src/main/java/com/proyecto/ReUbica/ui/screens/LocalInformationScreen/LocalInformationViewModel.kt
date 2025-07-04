package com.proyecto.ReUbica.ui.screens.LocalInformationScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Tag
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.model.emprendimiento.RedesSociales
import com.proyecto.ReUbica.data.model.emprendimiento.UpdateEmprendimientoRequest
import com.proyecto.ReUbica.data.repository.EmprendimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmprendimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val emprendimientoRepository = EmprendimientoRepository()
    private val sessionManager = UserSessionManager(application)

    private val _emprendimiento = MutableStateFlow<EmprendimientoModel?>(null)
    val emprendimiento: StateFlow<EmprendimientoModel?> = _emprendimiento.asStateFlow()

    private val _redesSociales = MutableStateFlow<RedesSociales?>(null)
    val redesSociales = _redesSociales.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _success = MutableStateFlow<Boolean?>(null)
    val success: StateFlow<Boolean?> = _success.asStateFlow()

    private val TAG = "EmprendimientoViewModel"

    fun cargarMiEmprendimiento() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val token = sessionManager.getToken()
                if (token.isNullOrBlank()) {
                    _error.value = "Token no encontrado. Inicie sesión nuevamente."
                    _loading.value = false
                    return@launch
                }
                val response = emprendimientoRepository.getMiEmprendimiento(token)
                Log.e("EmprendimientoViewModel", _emprendimiento.value.toString())
                if (response.isSuccessful) {
                    _emprendimiento.value = response.body()
                    _redesSociales.value = response.body()?.redes_sociales
                } else if (response.code() == 404) {
                    _emprendimiento.value = null
                } else {
                    _error.value = "Error al obtener emprendimiento: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    private fun EmprendimientoModel.toUpdateRequest(): UpdateEmprendimientoRequest {
        return UpdateEmprendimientoRequest(
            nombre = this.nombre ?: "",
            descripcion = this.descripcion ?: "",
            categoriasPrincipales = this.categoriasPrincipales,
            categoriasSecundarias = this.categoriasSecundarias,
            direccion = this.direccion ?: "",
            emprendimientoPhone = this.emprendimientoPhone ?: "",
            redes_sociales = this.redes_sociales ?: RedesSociales()
        )
    }

    fun updateEmprendimiento(updateData: UpdateEmprendimientoRequest) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            _success.value = null
            try {
                val token = sessionManager.getToken()
                if (token.isNullOrBlank()) {
                    _error.value = "Token no disponible, inicia sesión de nuevo"
                    _loading.value = false
                    return@launch
                }

                val response = emprendimientoRepository.updateEmprendimiento(token, updateData)

                _success.value = response.isSuccessful

                if(response.isSuccessful){
                    cargarMiEmprendimiento()
                    Log.d(TAG, "Emprendimiento actualizado correctamente")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
                _success.value = false
            } finally {
                _loading.value = false
            }
        }
    }
    fun updateEmprendimientoLogo(filePath: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            _success.value = null
            try {
                val token = sessionManager.getToken()
                if (token.isNullOrBlank()) {
                    _error.value = "Token no disponible, inicia sesión de nuevo"
                    _loading.value = false
                    return@launch
                }
                val response = emprendimientoRepository.updateEmprendimientoLogo(token, filePath)
                _success.value = response.isSuccessful
                if (response.isSuccessful) {
                    cargarMiEmprendimiento() // Recarga la info actualizada del emprendimiento
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
                _success.value = false
            } finally {
                _loading.value = false
            }
        }
    }


}