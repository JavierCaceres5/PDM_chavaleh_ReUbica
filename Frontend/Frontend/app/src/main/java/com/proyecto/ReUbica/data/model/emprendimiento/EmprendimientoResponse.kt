package com.proyecto.ReUbica.data.model.emprendimiento

import com.proyecto.ReUbica.data.model.user.UserProfile

data class EmprendimientoResponse(
    val token: String,
    val user: UserProfile,
    val emprendimiento: EmprendimientoModel,
    val message: String
)
