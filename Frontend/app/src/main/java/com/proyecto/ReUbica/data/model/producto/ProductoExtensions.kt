package com.proyecto.ReUbica.data.model.producto

fun ProductoModel.toProductoResponse(): ProductoResponse {
    return ProductoResponse(
        id = id.toString(),
        nombre = nombre.orEmpty(),
        descripcion = descripcion.orEmpty(),
        precio = precio ?: 0.0,
        product_image = product_image,
        created_at = created_at.orEmpty(),
        updated_at = updated_at.orEmpty(),
        emprendimientoID = emprendimientoID.toString()
    )
}