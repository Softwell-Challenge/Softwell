package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.LoginRequest
import br.com.fiap.softwell.model.LoginResponse
import br.com.fiap.softwell.model.RegisterRequest // ✅ 1. Importa o modelo de dados do registro
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("softwell/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // ✅✅✅ 2. ADICIONE ESTE MÉTODO FALTANTE ✅✅✅
    // Este é o método que a RegisterScreen está tentando chamar.
    @POST("softwell/auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<String>
}
