package br.com.fiap.softwell.service

import br.com.fiap.softwell.components.MoodButton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// class
object RetrofitFactory {
    private val URL = "http://10.0.2.2:8080/"
    private val retrofitFactory = Retrofit
        .Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun getMoodService(): MoodService {
        return retrofitFactory.create(MoodService::class.java)
    }

    // NOVO: Método para obter o serviço de Atividades (Apoio)
    fun getActivityService(): ActivityApiService {
        return retrofitFactory.create(ActivityApiService::class.java)
    }
}