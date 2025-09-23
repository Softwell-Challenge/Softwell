package br.com.fiap.softwell.service

import retrofit2.http.GET
import br.com.fiap.softwell.model.HumorData

interface HumorApiService {
    @GET("api/humores")
    suspend fun getHumorData(): HumorData
}