package com.proyecto.ReUbica.data.model.producto

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
