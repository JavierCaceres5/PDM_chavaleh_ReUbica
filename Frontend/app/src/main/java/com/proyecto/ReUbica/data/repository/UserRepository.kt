package com.proyecto.ReUbica.data.repository


import android.util.Log
import com.proyecto.ReUbica.data.model.password.GenericResponse
import com.proyecto.ReUbica.data.model.password.ResetPasswordRequest
import com.proyecto.ReUbica.data.model.password.SendResetCodeRequest
import com.proyecto.ReUbica.data.model.user.UpdateProfileRequest
import retrofit2.Response
import com.proyecto.ReUbica.data.model.user.UserLoginRequest
import com.proyecto.ReUbica.data.model.user.UserLoginResponse
import com.proyecto.ReUbica.data.model.user.UserRegisterRequest
import com.proyecto.ReUbica.network.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import com.proyecto.ReUbica.data.model.user.UserProfile
import okhttp3.RequestBody.Companion.toRequestBody
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

    suspend fun updateProfileWithImage(
        token: String,
        firstname: String,
        lastname: String,
        email: String,
        phone: String,
        imageFile: File?
    ): Result<UserProfile> {
        val TAG = "RETROFIT_UPDATE_PROFILE"

        val firstnameBody = firstname.toRequestBody("text/plain".toMediaTypeOrNull())
        val lastnameBody = lastname.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = imageFile?.let {
            val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("user_icon", it.name, reqFile)
        }

        Log.d(TAG, "Enviando updateProfileWithImage:")
        Log.d(TAG, "Token: Bearer $token")
        Log.d(TAG, "firstname: $firstname")
        Log.d(TAG, "lastname: $lastname")
        Log.d(TAG, "email: $email")
        Log.d(TAG, "phone: $phone")
        Log.d(TAG, "imageFile: ${imageFile?.absolutePath ?: "null"}")

        val response = try {
            api.updateProfile(
                "Bearer $token",
                firstnameBody,
                lastnameBody,
                emailBody,
                phoneBody,
                imagePart
            )
        } catch (e: Exception) {
            Log.e("RETROFIT_ERROR", "Excepción al llamar a updateProfile: ${e.message}", e)
            return Result.failure(e)
        }


        Log.d(TAG, "HTTP code: ${response.code()}")
        Log.d(TAG, "isSuccessful: ${response.isSuccessful}")
        Log.d(TAG, "body: ${response.body()}")
        Log.d(TAG, "errorBody: ${response.errorBody()?.string()}")

        return if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Error HTTP ${response.code()}: ${response.errorBody()?.string()}"))
        }
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
