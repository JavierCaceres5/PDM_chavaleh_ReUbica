package com.proyecto.ReUbica.data.model.user

import java.util.UUID

data class UserProfile(
    val id: UUID,
    val firstname: String?,
    val lastname: String?,
    val phone: String?,
    val email: String?,
    val user_icon: String?,
    val created_at: String?,
    val updated_at: String?,
    val user_role: String?
)