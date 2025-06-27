package com.proyecto.ReUbica.ui.screens.HomeScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.repository.EmprendimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EmprendimientoRepository()

    private val _resultadosByCategory = MutableStateFlow<List<EmprendimientoModel>>(emptyList())
    val resultadosByCategory: StateFlow<List<EmprendimientoModel>> = _resultadosByCategory

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun searchEmprendimientoByCategory(categoria: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.searchByCategory(categoria)
                if (response.isSuccessful) {
                    _resultadosByCategory.value = response.body() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.message()}"
                    _resultadosByCategory.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Error de red: ${e.message}")
                _error.value = "Ocurrió un error de red."
                _resultadosByCategory.value = emptyList()
            }
            _loading.value = false
        }
    }
}
