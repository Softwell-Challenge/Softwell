package br.com.fiap.softwell.service

// 1. Importações necessárias para o Interceptor de Autenticação
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// ... (imports)

object RetrofitFactory {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    // ... (okHttpClient e instância do retrofit) ...
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


    // --- MÉTODOS PARA OBTER CADA SERVIÇO ---

    // ✅✅✅ CORREÇÃO PRINCIPAL AQUI ✅✅✅
    // O nome do método deve retornar a interface "HumorApiService", não "MoodService".
    // Dentro de RetrofitFactory.kt
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




//package br.com.fiap.softwell.service
//
//import br.com.fiap.softwell.components.MoodButton
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//// class
//object RetrofitFactory {
//    private val URL = "http://10.0.2.2:8080/"
//    private val retrofitFactory = Retrofit
//        .Builder()
//        .baseUrl(URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//    fun getMoodService(): MoodService {
//        return retrofitFactory.create(MoodService::class.java)
//    }
//
//    // NOVO: Método para obter o serviço de Atividades (Apoio)
//    fun getActivityService(): ActivityApiService {
//        return retrofitFactory.create(ActivityApiService::class.java)
//    }
//
//    // NOVO: Método para obter o serviço de Respostas Psicossociais
//    fun getPsychoSocialService(): PsychoSocialApiService {
//        return retrofitFactory.create(PsychoSocialApiService::class.java)
//    }
//
//    fun getAuthService(): AuthService {
//        return retrofit.create(AuthService::class.java)
//    }
//}