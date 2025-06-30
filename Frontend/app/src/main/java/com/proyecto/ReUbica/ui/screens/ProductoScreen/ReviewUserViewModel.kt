package com.proyecto.ReUbica.ui.screens.ProductoScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.model.user.UserProfile
import com.proyecto.ReUbica.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewUserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository()


    private val _userProfiles = MutableStateFlow<Map<String, UserProfile>>(emptyMap())
    val userProfiles: StateFlow<Map<String, UserProfile>> = _userProfiles

    fun getUserById(token: String, userId: String) {
        Log.d("UserViewModel", "Token enviado: $token, UserID: $userId")

        viewModelScope.launch {
            try {
                val response = repository.getUserById(token, userId)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _userProfiles.value = _userProfiles.value + (userId to user)
                    }
                } else {
                    Log.e("UserViewModel", "Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Excepción: ${e.localizedMessage}", e)
            }
        }
    }
}
