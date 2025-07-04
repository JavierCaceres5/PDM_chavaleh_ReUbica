package com.proyecto.ReUbica.data.api

import com.proyecto.ReUbica.data.model.user.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

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

    @Multipart
    @PUT("users/updateProfile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("firstname") firstname: okhttp3.RequestBody,
        @Part("lastname") lastname: okhttp3.RequestBody,
        @Part("email") email: okhttp3.RequestBody,
        @Part("phone") phone: okhttp3.RequestBody,
        @Part user_icon: MultipartBody.Part?
    ): Response<UserProfile>

}
