package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.Mood
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface MoodService {
    //https://682d239e4fae18894754fecd.mockapi.io/api/v1/
    //https://682d239e4fae18894754fecd.mockapi.io/api/v1/user_mood
    @GET("api/v1/user_mood")
    suspend fun getAllMood(): Response<List<Mood>>
//    fun getAllMood(): Call<List<Mood>>
}