package com.proyecto.ReUbica.data.model.emprendimiento

data class UpdateEmprendimientoRequest (
    val nombre: String,
    val descripcion: String,
    val categoriasPrincipales: List<String>?,
    val categoriasSecundarias: List<String>?,
    val direccion: String,
    val emprendimientoPhone: String,
    val redes_sociales: RedesSociales,
)