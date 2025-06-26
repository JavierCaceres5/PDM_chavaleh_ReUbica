package com.proyecto.ReUbica.data.model.emprendimiento

import java.util.UUID

data class EmprendimientoModel (
    val id: UUID,
    val nombre: String?,
    val descripcion: String?,
    val categoriasSecundarias: List<String>,
    val logo: String?,
    val direccion: String?,
    val emprendimientoPhone: String?,
    val redes_sociales: RedesSociales?,
    val userID: UUID,
    val latitud: Double?,
    val longitud: Double?,
    val categoriasPrincipales: List<String>,
    val created_at: String?,
    val updated_at: String?
)

data class RedesSociales(
    val Instagram: String? = null,
    val Facebook: String? = null,
    val TikTok: String? = null,
    val Twitter: String? = null,
)