package com.proyecto.ReUbica.data.model.emprendimiento

import com.proyecto.ReUbica.data.model.user.UserProfile

data class EmprendimientoResponse(
    val updatedToken: String,
    val emprendimiento: EmprendimientoModel,
    val message: String
)
