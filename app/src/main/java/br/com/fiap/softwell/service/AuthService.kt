package br.com.fiap.softwell.service
import br.com.fiap.softwell.model.LoginRequest
import br.com.fiap.softwell.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface AuthService {
    @POST("/softwell/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}