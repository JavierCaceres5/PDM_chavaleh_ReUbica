package com.proyecto.ReUbica.data.repository

import com.proyecto.ReUbica.data.model.password.GenericResponse
import com.proyecto.ReUbica.data.model.password.ResetPasswordRequest
import com.proyecto.ReUbica.data.model.password.SendResetCodeRequest
import com.proyecto.ReUbica.data.model.user.UpdateProfileRequest
import retrofit2.Response
import com.proyecto.ReUbica.data.model.user.UserLoginRequest
import com.proyecto.ReUbica.data.model.user.UserLoginResponse
import com.proyecto.ReUbica.data.model.user.UserProfile
import com.proyecto.ReUbica.data.model.user.UserRegisterRequest
import com.proyecto.ReUbica.network.RetrofitInstance

class UserRepository {

    private val api = RetrofitInstance.api

    suspend fun register(request: UserRegisterRequest): Response<UserLoginResponse> {
        return api.register(request)
    }

    suspend fun login(request: UserLoginRequest): Response<UserLoginResponse> {
        return api.login(request)
    }

    suspend fun deleteAccount(token: String): Response<Unit> {
        return api.deleteProfile("Bearer $token")
    }

    suspend fun updateAccount(token: String, updateData: UpdateProfileRequest): Response<Unit> {
        return api.updateProfile("Bearer $token", updateData)
    }

    suspend fun getUserById(token: String, userId: String): Response<UserProfile> {
        return api.getUserById("Bearer $token", userId)
    }

    suspend fun sendResetCode(request: SendResetCodeRequest): Response<GenericResponse> {
        return api.sendResetCode(request)
    }

    suspend fun resetPassword(request: ResetPasswordRequest): Response<GenericResponse> {
        return api.resetPassword(request)
    }

}
