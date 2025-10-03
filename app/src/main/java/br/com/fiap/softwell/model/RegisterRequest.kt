package br.com.fiap.softwell.model

data class RegisterRequest(
    val username: String,
    val cpf: String,
    val password: String,
    val roles: List<String>
)
    