package com.proyecto.ReUbica.network

import com.proyecto.ReUbica.data.api.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://reubica-backend-894246362783.us-central1.run.app/api/"

    val api: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }
}