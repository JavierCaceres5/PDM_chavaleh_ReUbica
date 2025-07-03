package com.proyecto.ReUbica.data.model.user

data class UserLoginResponse(
    val token: String,
    val user: UserProfile,
    val message: String
)
