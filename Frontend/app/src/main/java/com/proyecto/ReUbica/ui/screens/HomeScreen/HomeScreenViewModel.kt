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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.proyecto.ReUbica.data.repository.FavoritoRepository
import com.proyecto.ReUbica.data.local.UserSessionManager



class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EmprendimientoRepository()

    private val _resultadosByCategory = MutableStateFlow<List<EmprendimientoModel>>(emptyList())
    val resultadosByCategory: StateFlow<List<EmprendimientoModel>> = _resultadosByCategory

    private val _todosLosEmprendimientos = MutableStateFlow<List<EmprendimientoModel>>(emptyList())
    val todosLosEmprendimientos: StateFlow<List<EmprendimientoModel>> = _todosLosEmprendimientos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun searchEmprendimientoByCategory(categoria: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.searchByCategory(categoria)
                }
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

    private var isDataLoaded = false

    fun obtenerTodosLosEmprendimientos(token: String) {
        if (isDataLoaded) return
        isDataLoaded = true

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getAllEmprendimientos(token)
                }
                if (response.isSuccessful) {
                    _todosLosEmprendimientos.value = response.body() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.message()}"
                    _todosLosEmprendimientos.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Error al obtener todos: ${e.message}")
                _error.value = "Ocurrió un error al cargar los emprendimientos."
                _todosLosEmprendimientos.value = emptyList()
            }
            _loading.value = false
        }
    }
    val favoritos = MutableStateFlow<List<String>>(emptyList())

    fun cargarFavoritos(sessionManager: UserSessionManager) {
        viewModelScope.launch {
            val token = sessionManager.getToken() ?: return@launch
            val favs = FavoritoRepository.getFavoritos(token, "emprendimiento")
            favoritos.value = favs
        }
    }

    fun toggleFavorito(sessionManager: UserSessionManager, id: String) {
        viewModelScope.launch {
            val token = sessionManager.getToken() ?: return@launch
            val success = FavoritoRepository.toggleFavorito(token, "emprendimiento", id)
            if (success) {
                val actual = favoritos.value.toMutableList()
                if (actual.contains(id)) {
                    actual.remove(id)
                } else {
                    actual.add(id)
                }
                favoritos.value = actual
            }
        }
    }

}
