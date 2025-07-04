package com.proyecto.ReUbica.ui.screens.FavoriteScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.data.repository.FavoritoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var userSessionManager: UserSessionManager

    fun setUserSessionManager(manager: UserSessionManager) {
        userSessionManager = manager
    }

    private val _favoritosEmprendimientos = MutableStateFlow<List<EmprendimientoModel>>(emptyList())
    val favoritosEmprendimientos: StateFlow<List<EmprendimientoModel>> = _favoritosEmprendimientos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun cargarFavoritos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val token = userSessionManager.getToken() ?: return@launch
                val favoritos = FavoritoRepository.getFavoritos(token, "emprendimiento")
                val emprendimientos = FavoritoRepository.getEmprendimientosFavoritos(token)
                _favoritosEmprendimientos.value = emprendimientos

            } catch (e: Exception) {
                _favoritosEmprendimientos.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun toggleFavorito(id: String) {
        viewModelScope.launch {
            val token = userSessionManager.getToken() ?: return@launch
            val success = FavoritoRepository.toggleFavorito(token, "emprendimiento", id)
            if (success) {
                cargarFavoritos()
            }
        }
    }
}
