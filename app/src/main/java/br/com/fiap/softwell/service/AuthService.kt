package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.LoginRequest
import br.com.fiap.softwell.model.LoginResponse
import br.com.fiap.softwell.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("softwell/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("softwell/auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<String>
}
