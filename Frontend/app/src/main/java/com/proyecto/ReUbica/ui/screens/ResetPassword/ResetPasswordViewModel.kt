package com.proyecto.ReUbica.ui.screens.ResetPassword

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.model.password.GenericResponse
import com.proyecto.ReUbica.data.model.password.ResetPasswordRequest
import com.proyecto.ReUbica.data.model.password.SendResetCodeRequest
import com.proyecto.ReUbica.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(): ViewModel() {

    private val repository = UserRepository()

    private val _resetPasswordRequest = MutableStateFlow<ResetPasswordRequest?>(null)
    val resetPasswordRequest: StateFlow<ResetPasswordRequest?> = _resetPasswordRequest

    private val _sendResetCodeRequest = MutableStateFlow<SendResetCodeRequest?>(null)
    val sendResetCodeRequest: StateFlow<SendResetCodeRequest?> = _sendResetCodeRequest

    private val _genericResponse = MutableStateFlow<GenericResponse?>(null)
    val genericResponse: StateFlow<GenericResponse?> = _genericResponse

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val TAG = "ResetPasswordViewModel"

    fun setError(message: String) {
        _error.value = message
    }

    fun sendResetCode(email: String) {

        if (email.isBlank()) {
            _error.value = "El correo no puede estar vacío"
            return
        }

        _loading.value = true
        _error.value = null
        _successMessage.value = null

        viewModelScope.launch {
            try {
                val response = repository.sendResetCode(SendResetCodeRequest(email))
                if (response.isSuccessful) {
                    _successMessage.value = response.body()?.message ?: "Código enviado"
                    Log.d(TAG, "Código enviado exitosamente a $email")
                } else {
                    _error.value = response.body()?.error ?: "Error al enviar código"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sendResetCode", e)
                _error.value = "Error de red o servidor"
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetPassword(email: String, code: String, newPassword: String, confirmNewPassword: String) {
        if (email.isBlank() || code.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
            _error.value = "Completa todos los campos"
            return
        }
        if (newPassword != confirmNewPassword) {
            _error.value = "Las contraseñas no coinciden"
            return
        }

        _loading.value = true
        _error.value = null
        _successMessage.value = null

        viewModelScope.launch {
            try {
                val request = ResetPasswordRequest(email, code, newPassword, confirmNewPassword)
                val response = repository.resetPassword(request)
                if (response.isSuccessful) {
                    _successMessage.value = response.body()?.message ?: "Contraseña actualizada"
                    Log.d(TAG, "Contraseña actualizada exitosamente para $email")
                } else {
                    _error.value = response.body()?.error ?: "Error al actualizar contraseña"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error resetPassword", e)
                _error.value = "Error de red o servidor"
            } finally {
                _loading.value = false
            }
        }
    }

}