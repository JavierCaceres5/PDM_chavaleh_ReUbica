package com.proyecto.ReUbica.data.model.favorito

import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel

data class FavoritoResponse(
    val id: String,
    val tipo_objetivo: String,
    val Comercio: EmprendimientoModel?
)
