package com.proyecto.ReUbica.ui.screens.FavoriteScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

sealed class Favorito {
    data class Comercio(
        val nombre: String,
        val departamento: String,
        val categoria: String
    ) : Favorito()

    data class Producto(
        val id: String,
        val nombre: String,
        val precio: Double
    ) : Favorito()
}

class FavoritosViewModel : ViewModel() {
    private val _favoritos = mutableStateListOf<Favorito>()
    val favoritos: List<Favorito> = _favoritos

    // Para comercios
    fun toggleFavoritoComercio(nombre: String, departamento: String, categoria: String) {
        val favorito = Favorito.Comercio(nombre, departamento, categoria)
        if (_favoritos.contains(favorito)) {
            _favoritos.remove(favorito)
        } else {
            _favoritos.add(favorito)
        }
    }

    fun isFavoritoComercio(nombre: String): Boolean {
        return _favoritos.any { it is Favorito.Comercio && it.nombre == nombre }
    }

    // Para productos
    fun toggleFavoritoProducto(id: String, nombre: String, precio: Double) {
        val favorito = Favorito.Producto(id, nombre, precio)
        if (_favoritos.contains(favorito)) {
            _favoritos.remove(favorito)
        } else {
            _favoritos.add(favorito)
        }
    }

    fun isFavoritoProducto(id: String): Boolean {
        return _favoritos.any { it is Favorito.Producto && it.id == id }
    }
}

