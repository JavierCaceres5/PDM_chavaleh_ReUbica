package com.proyecto.ReUbica.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.proyecto.ReUbica.data.model.user.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

data class UserSession(
    val token: String,
    val userProfile: UserProfile
)

class UserSessionManager(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore("user_prefs")
        private val TOKEN_KEY = stringPreferencesKey("TOKEN_KEY")
        private val USER_KEY = stringPreferencesKey("USER_KEY")
    }

    private val gson = Gson()

    suspend fun saveUserSession(token: String, user: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_KEY] = gson.toJson(user)
        }
    }

    val userSessionFlow: Flow<UserSession?> = context.dataStore.data.map { prefs ->
        val token = prefs[TOKEN_KEY]
        val userJson = prefs[USER_KEY]
        if (token != null && userJson != null) {
            val user = gson.fromJson(userJson, UserProfile::class.java)
            UserSession(token, user)
        } else null
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[TOKEN_KEY] }
            .first()
    }



}
