package com.proyecto.ReUbica.ui.screens.RegisterScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.user.UserProfile
import com.proyecto.ReUbica.data.model.user.UserRegisterRequest
import com.proyecto.ReUbica.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository()
    private val sessionManager = UserSessionManager(application)

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun register(user: UserRegisterRequest) {
        viewModelScope.launch {
            _loading.value = true
            val response = repository.register(user)
            Log.e("Registro", "Error de emprendimiento: ${_user.value}")
            if (response.isSuccessful) {
                val userResponse = response.body()?.user
                val tokenResponse = response.body()?.token
                if (userResponse != null && tokenResponse != null) {
                    _user.value = userResponse
                    _token.value = tokenResponse
                    _error.value = null
                    sessionManager.saveUserSession(tokenResponse, userResponse)
                } else {
                    _error.value = "Error: datos de usuario o token nulos"
                }
            } else {
                _error.value = "Error de registro: ${response.message()}"
                Log.e("UserViewModel", "Error de registro: ${response.body()?.message}")
            }
            _loading.value = false
        }
    }

    fun setUser(userProfile: UserProfile){
        _user.value = userProfile
    }
}