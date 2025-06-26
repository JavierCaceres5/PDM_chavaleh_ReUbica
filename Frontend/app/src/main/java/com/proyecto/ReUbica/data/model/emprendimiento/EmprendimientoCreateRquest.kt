package com.proyecto.ReUbica.data.model.emprendimiento

import java.util.UUID

data class EmprendimientoCreateRequest(
    val nombre: String,
    val descripcion: String,
    val categoriasPrincipales: List<String>,
    val categoriasSecundarias: List<String>,
    val logo: String? = null,
    val direccion: String,
    val redes_sociales: RedesSociales,
    val latitud: Double,
    val longitud: Double,
    val emprendimientoPhone: String
)