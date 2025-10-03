package br.com.fiap.softwell.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = AuthTokenManager.getToken()
            val originalRequest = chain.request()
            val newRequest = if (token != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }
            chain.proceed(newRequest)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getMoodService(): HumorApiService {
        return retrofit.create(HumorApiService::class.java)
    }

    fun getActivityService(): ActivityApiService {
        return retrofit.create(ActivityApiService::class.java)
    }

    fun getPsychoSocialService(): PsychoSocialApiService {
        return retrofit.create(PsychoSocialApiService::class.java)
    }

    fun getAuthService(): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}