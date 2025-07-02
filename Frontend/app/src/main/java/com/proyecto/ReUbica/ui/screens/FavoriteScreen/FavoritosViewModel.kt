package com.proyecto.ReUbica.ui.screens.FavoriteScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

sealed class Favorito {
    data class Comercio(
        val nombre: String,
        val departamento: String,
        val categoria: String,
        val logo: String?
    ) : Favorito()

    data class Producto(
        val id: String,
        val nombre: String,
        val precio: Double?
    ) : Favorito()
}

class FavoritosViewModel : ViewModel() {
    private val _favoritos = mutableStateListOf<Favorito>()
    val favoritos: List<Favorito> = _favoritos

    // ✅ Para comercios
    fun toggleFavoritoComercio(nombre: String, departamento: String, categoria: String, logo: String?) {
        val exists = _favoritos.any {
            it is Favorito.Comercio &&
                    it.nombre == nombre &&
                    it.departamento == departamento &&
                    it.categoria == categoria
        }
        if (exists) {
            _favoritos.removeIf {
                it is Favorito.Comercio &&
                        it.nombre == nombre &&
                        it.departamento == departamento &&
                        it.categoria == categoria
            }
        } else {
            _favoritos.add(Favorito.Comercio(nombre, departamento, categoria, logo))
        }
    }

    fun isFavoritoComercio(nombre: String): Boolean {
        return _favoritos.any { it is Favorito.Comercio && it.nombre == nombre }
    }

    // Para productos
    fun toggleFavoritoProducto(id: String, nombre: String, precio: Double?) {
        val exists = _favoritos.any { it is Favorito.Producto && it.id == id }
        if (exists) {
            _favoritos.removeIf { it is Favorito.Producto && it.id == id }
        } else {
            _favoritos.add(Favorito.Producto(id, nombre, precio))
        }
    }

    fun isFavoritoProducto(id: String): Boolean {
        return _favoritos.any { it is Favorito.Producto && it.id == id }
    }
}
