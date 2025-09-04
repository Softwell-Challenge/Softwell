package br.com.fiap.softwell.service

import br.com.fiap.softwell.components.MoodButton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    private val URL = "https://682d239e4fae18894754fecd.mockapi.io/"
    private val retrofitFactory = Retrofit
        .Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun getMoodService(): MoodService {
        return retrofitFactory.create(MoodService::class.java)
    }
}