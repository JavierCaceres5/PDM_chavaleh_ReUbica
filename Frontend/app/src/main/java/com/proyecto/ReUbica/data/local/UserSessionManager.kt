package com.proyecto.ReUbica.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import com.proyecto.ReUbica.data.model.user.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class UserSession(
    val token: String,
    val userProfile: UserProfile
)

class UserSessionManager(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore("user_prefs")
        private val TOKEN_KEY = stringPreferencesKey("TOKEN_KEY")
        private val USER_KEY = stringPreferencesKey("USER_KEY")
        private val EMPRENDIMIENTO_ID_KEY = stringPreferencesKey("EMPRENDIMIENTO_ID")
        private val PRODUCTO_ID_KEY = stringPreferencesKey("PRODUCTO_ID")
    }

    private val gson = Gson()
    private val _userSession = MutableStateFlow<UserSession?>(null)
    val userSessionFlow: StateFlow<UserSession?> = _userSession

    suspend fun saveEmprendimientoID(id: String) {
        context.dataStore.edit { prefs ->
            prefs[EMPRENDIMIENTO_ID_KEY] = id
        }
    }

    suspend fun getEmprendimientoID(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[EMPRENDIMIENTO_ID_KEY] }
            .first()
    }

    suspend fun saveProductoID(id: String) {
        context.dataStore.edit { prefs ->
            prefs[PRODUCTO_ID_KEY] = id
        }
    }

    suspend fun getProductoID(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[PRODUCTO_ID_KEY] }
            .first()
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val token = getToken()
            val user = getUserProfile(token ?: "")
            if (token != null && user != null) {
                _userSession.emit(UserSession(token, user))
            }
        }
    }

    suspend fun saveUserSession(token: String, user: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_KEY] = gson.toJson(user)
        }
        _userSession.emit(UserSession(token, user))
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
        _userSession.emit(null)
    }

    suspend fun getToken(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[TOKEN_KEY] }
            .first()
    }

    suspend fun getUserProfile(token: String): UserProfile? {
        return context.dataStore.data
            .map { prefs ->
                val savedToken = prefs[TOKEN_KEY]
                val userJson = prefs[USER_KEY]
                if (savedToken == token && userJson != null) {
                    gson.fromJson(userJson, UserProfile::class.java)
                } else null
            }
            .first()
    }

    suspend fun actualizarSesionConNuevoToken(updatedToken: String) {
        val jwt = JWT(updatedToken)
        val nuevoRol = jwt.getClaim("role").asString() ?: return
        val perfilActual = userSessionFlow.first()?.userProfile ?: return
        val perfilActualizado = perfilActual.copy(user_role = nuevoRol)
        saveUserSession(updatedToken, perfilActualizado)
    }

}
