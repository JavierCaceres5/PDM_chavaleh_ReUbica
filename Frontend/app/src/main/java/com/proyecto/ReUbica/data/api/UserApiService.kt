package com.proyecto.ReUbica.data.api

import com.proyecto.ReUbica.data.model.user.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("users/register")
    suspend fun register(
        @Body request: UserRegisterRequest
    ): Response<UserLoginResponse>

    @POST("users/login")
    suspend fun login(
        @Body request: UserLoginRequest
    ): Response<UserLoginResponse>

}
