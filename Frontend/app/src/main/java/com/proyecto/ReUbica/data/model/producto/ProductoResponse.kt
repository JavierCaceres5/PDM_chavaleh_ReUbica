package com.proyecto.ReUbica.data.model.producto

import com.proyecto.ReUbica.ui.screens.FavoriteScreen.Favorito.Producto

data class ProductoResponse(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val product_image: String?,
    val created_at: String,
    val updated_at: String,
    val emprendimientoID: String
) {
    fun toProducto(): ProductoModel {
        return ProductoModel(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            product_image = product_image ?: "",
            created_at = created_at,
            updated_at = updated_at,
            emprendimientoID = emprendimientoID
        )
    }

    fun ProductoModel.toProductoResponse(): ProductoResponse {
        return ProductoResponse(
            id = this.id,
            nombre = this.nombre,
            descripcion = this.descripcion,
            precio = this.precio,
            product_image = this.product_image,
            created_at = this.created_at,
            updated_at = this.updated_at,
            emprendimientoID = this.emprendimientoID
        )
    }

}
