package com.proyecto.ReUbica.data.api

import com.proyecto.ReUbica.data.model.password.GenericResponse
import com.proyecto.ReUbica.data.model.password.ResetPasswordRequest
import com.proyecto.ReUbica.data.model.password.SendResetCodeRequest
import com.proyecto.ReUbica.data.model.user.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {

    @POST("users/register")
    suspend fun register(
        @Body request: UserRegisterRequest
    ): Response<UserLoginResponse>

    @POST("users/login")
    suspend fun login(
        @Body request: UserLoginRequest
    ): Response<UserLoginResponse>

    @DELETE("users/deleteProfile")
    suspend fun deleteProfile(
        @Header("Authorization") token: String)
    : Response<Unit>

    @PUT("users/updateProfile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body updateData: UpdateProfileRequest
    ): Response<Unit>

    @GET("users/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") userId: String
    ): Response<UserProfile>

    @POST("users/sendResetCode")
    suspend fun sendResetCode(
        @Body request: SendResetCodeRequest
    ): Response<GenericResponse>

    @POST("users/resetPassword")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<GenericResponse>

}
