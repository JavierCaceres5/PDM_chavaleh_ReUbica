package com.proyecto.ReUbica.ui.screens.PersonalInformationScreen

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSession
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.user.UpdateProfileRequest
import com.proyecto.ReUbica.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonalInformationViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = UserSessionManager(application)
    private val repository = UserRepository()

    private val _userSession = MutableStateFlow<UserSession?>(null)
    val userSession: StateFlow<UserSession?> = _userSession

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow<Boolean?>(null)
    val success: StateFlow<Boolean?> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            sessionManager.userSessionFlow.collect { session ->
                _userSession.value = session
            }
        }
    }

    fun updateProfile(updatedProfile: UpdateProfileRequest) {
        viewModelScope.launch {
            try {
                val token = sessionManager.getToken() ?: return@launch
                val response = repository.updateAccount(token, updatedProfile)

                if (response.isSuccessful) {
                    val currentSession = _userSession.value
                    if (currentSession != null) {
                        val newUserProfile = currentSession.userProfile.copy(
                            firstname = updatedProfile.firstname,
                            lastname = updatedProfile.lastname,
                            email = updatedProfile.email,
                            phone = updatedProfile.phone,
                            user_icon = updatedProfile.user_icon ?: currentSession.userProfile.user_icon
                        )
                        sessionManager.saveUserSession(token, newUserProfile)
                    }
                }
            } catch (e: Exception) {
                e.message
            }
        }
    }


}