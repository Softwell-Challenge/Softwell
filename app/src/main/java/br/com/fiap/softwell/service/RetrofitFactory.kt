package br.com.fiap.softwell.service

// 1. Importações necessárias para o Interceptor de Autenticação
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    // URL base da sua API
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // 2. Cria um cliente OkHttp com o interceptor de autenticação
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            // Recupera o token PURO que foi salvo no AuthTokenManager
            val token = AuthTokenManager.getToken()
            val originalRequest = chain.request()

            // Cria uma nova requisição adicionando o cabeçalho de autorização
            val newRequest = if (token != null) {
                originalRequest.newBuilder()
                    // ✅ CORREÇÃO: Adicionar o prefixo "Bearer " aqui
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                // Se não houver token, envia a requisição original (para telas de login/cadastro)
                originalRequest
            }
            // Continua a chamada com a nova requisição (que agora tem o token)
            chain.proceed(newRequest)
        }
        .build()

    // 3. Cria a instância principal do Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient) // <-- IMPORTANTE: Usa o cliente com o interceptor de autenticação
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // --- MÉTODOS PARA OBTER CADA SERVIÇO ---

    fun getMoodService(): MoodService {
        return retrofit.create(MoodService::class.java)
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