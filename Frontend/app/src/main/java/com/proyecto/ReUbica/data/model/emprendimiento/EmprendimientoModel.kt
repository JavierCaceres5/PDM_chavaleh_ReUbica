package com.proyecto.ReUbica.data.model.emprendimiento

import java.util.UUID

data class EmprendimientoModel (
    val id: UUID,
    val nombre: String?,
    val descripcion: String?,
    val categoriasSecundarias: String?,
    val logo: String?,
    val horarios_atencion: String?,
    val direccion: String?,
    val emprendimientoPhone: String?,
    val redes_sociales: RedesSociales?,
    val userID: UUID,
    val latitud: Double?,
    val longitud: Double?,
    val categoriasPrincipales: String?,
    val created_at: String?,
    val updated_at: String?
)

data class RedesSociales(
    val instagram: String?,
    val facebook: String?,
    val tiktok: String?,
    val twitter: String?,
    val whatsapp: String?
)