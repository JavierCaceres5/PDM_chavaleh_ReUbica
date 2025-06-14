package com.poyecto.ReUbica.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Favorito(
    val nombre: String,
    val departamento: String,
    val categoria: String
)

class FavoritosViewModel : ViewModel() {
    private val _favoritos = mutableStateListOf<Favorito>()
    val favoritos: List<Favorito> = _favoritos

    fun toggleFavorito(nombre: String, departamento: String, categoria: String) {
        val existe = _favoritos.any { it.nombre == nombre }
        if (existe) {
            _favoritos.removeAll { it.nombre == nombre }
        } else {
            _favoritos.add(Favorito(nombre, departamento, categoria))
        }
    }

    fun isFavorito(nombre: String): Boolean {
        return _favoritos.any { it.nombre == nombre }
    }
}
