package com.proyecto.ReUbica.data.model.user

data class UpdateProfileRequest (
    val firstname: String,
    val lastname: String,
    val phone: String,
    val email: String,
    val user_icon: String? = null
)