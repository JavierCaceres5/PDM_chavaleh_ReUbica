package com.proyecto.ReUbica.network

import com.proyecto.ReUbica.data.api.EmprendimientoApiService
import com.proyecto.ReUbica.data.api.FavoritoApiService
import com.proyecto.ReUbica.data.api.ProductoApiService

import com.proyecto.ReUbica.data.api.ReviewApiService

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

    val emprendimientoApi: EmprendimientoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmprendimientoApiService::class.java)
    }


    val productoApi: ProductoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

            .create(ProductoApiService::class.java)

    }

    val reviewApi: ReviewApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReviewApiService::class.java)
    }

    val favoritoApi: FavoritoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FavoritoApiService::class.java)
    }

}