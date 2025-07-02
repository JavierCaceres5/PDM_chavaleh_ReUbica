package com.proyecto.ReUbica.data.model.producto

fun ProductoModel.toProductoResponse(): ProductoResponse {
    return ProductoResponse(
        id = this.id.toString(),
        nombre = this.nombre.toString(),
        descripcion = this.descripcion.toString(),
        precio = this.precio?: 0.0,
        product_image = this.product_image,
        created_at = this.created_at.toString(),
        updated_at = this.updated_at.toString(),
        emprendimientoID = this.emprendimientoID.toString()
    )
}
