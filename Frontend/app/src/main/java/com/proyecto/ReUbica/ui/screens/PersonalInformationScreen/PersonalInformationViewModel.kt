package com.proyecto.ReUbica.ui.screens.PersonalInformationScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.ReUbica.data.local.UserSession
import com.proyecto.ReUbica.data.local.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonalInformationViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = UserSessionManager(application)

    private val _userSession = MutableStateFlow<UserSession?>(null)
    val userSession: StateFlow<UserSession?> = _userSession

    init {
        viewModelScope.launch {
            sessionManager.userSessionFlow.collect { session ->
                _userSession.value = session
            }
        }
    }

}
