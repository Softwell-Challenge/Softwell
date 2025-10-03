package br.com.fiap.softwell.service

import android.util.Log
import com.auth0.android.jwt.JWT

object AuthTokenManager {

    private var authToken: String? = null

    fun saveToken(token: String?) {
        authToken = token
    }

    fun getToken(): String? {
        return authToken
    }

    fun getUserIdFromToken(): String? {
        return try {
            val jwt = JWT(authToken ?: return null)
            jwt.subject
        } catch (e: Exception) {
            null
        }
    }

    fun isUserAdmin(): Boolean {
        val token = authToken ?: return false

        try {
            val jwt = JWT(token)

            val rolesList = jwt.getClaim("roles").asList(String::class.java)

            Log.d("ADMIN_CHECK", "Roles lidas do token: $rolesList")

            val isAdmin = rolesList?.any { role -> role.equals("ADMIN", ignoreCase = true) } ?: false

            Log.d("ADMIN_CHECK", "O usuário é admin? -> $isAdmin")

            return isAdmin

        } catch (e: Exception) {
            Log.e("ADMIN_CHECK", "Falha ao processar a claim 'roles'. Assumindo 'false'.", e)
            return false
        }
    }

    fun clearToken() {
        authToken = null
    }
}
