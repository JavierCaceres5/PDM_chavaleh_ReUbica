package com.proyecto.ReUbica.data.model.password

data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val newPassword: String,
    val confirmNewPassword: String
)
