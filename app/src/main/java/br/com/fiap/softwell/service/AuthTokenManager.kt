package br.com.fiap.softwell.service

import com.auth0.android.jwt.JWT

// Objeto singleton para armazenar o token de autenticação em memória.
object AuthTokenManager {

    private var authToken: String? = null

    // Função para salvar o token após o login ser bem-sucedido
    fun saveToken(token: String?) {
        // Adiciona o prefixo "Bearer " se ele não estiver presente
        authToken = if (token != null && !token.startsWith("Bearer ")) {
            "Bearer $token"
        } else {
            token
        }
    }

    // Função para recuperar o token para ser usado nas chamadas da API
    fun getToken(): String? {
        return authToken
    }

    fun getUserIdFromToken(): String? {
        return try {
            val jwt = JWT(authToken?.removePrefix("Bearer ") ?: return null)
            // O "subject" (sub) do JWT geralmente contém o username ou userId.
            // Verifique no seu backend qual "claim" você usa para o ID.
            jwt.subject
        } catch (e: Exception) {
            null // Retorna nulo se o token for inválido ou expirar
        }
    }

    // Função para limpar o token ao fazer logout
    fun clearToken() {
        authToken = null
    }
}
