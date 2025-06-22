package com.proyecto.ReUbica.ui.screens.RegistroComercioScreens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoCreateRequest
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.model.user.UserProfile
import com.proyecto.ReUbica.data.repository.EmprendimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistroComercioViewModel(application: Application): AndroidViewModel(application) {

    private val repository = EmprendimientoRepository()

    private val _emprendimiento = MutableStateFlow<EmprendimientoModel?>(null)
    val emprendimiento: StateFlow<EmprendimientoModel?> = _emprendimiento

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    fun createEmprendmiento(emprendimiento: EmprendimientoCreateRequest){
        viewModelScope.launch {
            _loading.value = true
            val response = repository.createEmprendimiento(emprendimiento)
            Log.e("REGISTER EMPRENDIMIENTO", response.toString())
            if (response.isSuccessful){
                val emprendimientoResponse = response.body()?.emprendimiento
                if (emprendimientoResponse != null) {
                    _emprendimiento.value = emprendimientoResponse
                    _error.value = null
                    _success.value = true
                } else {
                    _error.value = "Error: datos de emprendimiento o token nulos"
                }
            }else {
                _error.value = "Error de creacion de emprendimiento: ${response.message()}"
                Log.e("EmprendimientoViewModel", "Error de emprendimiento: ${response.body()?.message}")
            }
            _loading.value = false
        }
    }
}