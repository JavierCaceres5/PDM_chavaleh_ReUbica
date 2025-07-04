package com.proyecto.ReUbica.ui.screens.EmprendimientosBuscar

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.repository.EmprendimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EmprendimientoRepository()

    private val _resultadosByNombre = MutableStateFlow<List<EmprendimientoModel>>(emptyList())
    val resultadosByNombre: StateFlow<List<EmprendimientoModel>> = _resultadosByNombre

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun searchEmprendimientoByNombre(nombre: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.searchByName(nombre)
                if (response.isSuccessful) {
                    _resultadosByNombre.value = response.body() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.message()}"
                    _resultadosByNombre.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("BuscarViewModel", "Error de red: ${e.message}")
                _error.value = "Ocurrió un error de red."
                _resultadosByNombre.value = emptyList()
            }
            _loading.value = false
        }
    }
}