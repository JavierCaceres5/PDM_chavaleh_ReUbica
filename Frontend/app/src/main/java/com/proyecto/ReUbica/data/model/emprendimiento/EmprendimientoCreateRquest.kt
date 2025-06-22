package com.proyecto.ReUbica.data.model.emprendimiento

import java.util.UUID

data class EmprendimientoCreateRequest(
    val nombre: String,
    val descripcion: String,
    val categoriasPrincipales: String,
    val categoriasSecundarias: String,
    val logo: String? = null,
    val horarios_atencion: String,
    val direccion: String,
    val redes_sociales: RedesSociales,
    val userID: UUID,
    val latitud: Double,
    val longitud: Double
)