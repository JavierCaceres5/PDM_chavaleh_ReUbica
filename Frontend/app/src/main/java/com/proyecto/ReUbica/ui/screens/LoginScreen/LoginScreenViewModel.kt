package com.proyecto.ReUbica.ui.screens.LoginScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.user.UserLoginRequest
import com.proyecto.ReUbica.data.model.user.UserProfile
import com.proyecto.ReUbica.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel(application: Application) : AndroidViewModel(application) {

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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            val response = repository.login(UserLoginRequest(email.trim(), password))
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
                val errorMsg = when (response.code()) {
                    404 -> "El correo no está registrado."
                    401 -> "La contraseña es incorrecta."
                    else -> "Error de login ${response.message()}"
                }
                _error.value = errorMsg
            }
            _loading.value = false
        }
    }
}
