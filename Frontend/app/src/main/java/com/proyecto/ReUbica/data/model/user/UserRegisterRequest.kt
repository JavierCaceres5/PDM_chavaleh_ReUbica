package com.proyecto.ReUbica.data.model.user

data class UserRegisterRequest(
    val firstname: String,
    val lastname: String,
    val phone: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val user_icon: String? = null
)