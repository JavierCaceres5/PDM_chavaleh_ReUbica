package com.proyecto.ReUbica.ui.screens.PersonalInformationScreen

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSession
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

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

    fun updateProfile(
        firstname: String?,
        lastname: String?,
        email: String?,
        phone: String?,
        uri: Uri? = null,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val token = sessionManager.getToken() ?: return@launch

                val user = _userSession.value?.userProfile ?: return@launch

                val finalFirstname = firstname?.takeIf { it.isNotBlank() } ?: user.firstname ?: ""
                val finalLastname = lastname?.takeIf { it.isNotBlank() } ?: user.lastname ?: ""
                val finalEmail = email?.takeIf { it.isNotBlank() } ?: user.email ?: ""
                val finalPhone = phone?.takeIf { it.isNotBlank() } ?: user.phone ?: ""

                val imageFile = uri?.let {
                    File.createTempFile("profile", ".jpg", context.cacheDir).apply {
                        context.contentResolver.openInputStream(uri)?.use { input ->
                            FileOutputStream(this).use { output -> input.copyTo(output) }
                        }
                    }
                }

                val result = repository.updateProfileWithImage(
                    token = token,
                    firstname = finalFirstname,
                    lastname = finalLastname,
                    email = finalEmail,
                    phone = finalPhone,
                    imageFile = imageFile
                )

                result.onSuccess { newUser ->
                    sessionManager.saveUserSession(token, newUser)
                    _userSession.value = UserSession(token, newUser)
                    _success.value = true
                }.onFailure { ex ->
                    _error.value = "Error al actualizar perfil: ${ex.message}"
                }
            } catch (e: Exception) {
                Log.e("updateProfile", "Error: ${e.message}")
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

}