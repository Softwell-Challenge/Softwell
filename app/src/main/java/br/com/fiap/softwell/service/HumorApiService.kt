package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.HumorData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface HumorApiService {
    @GET("api/humores")
    suspend fun getHumorData(): Response<List<HumorData>>

    @POST("api/humores")
    suspend fun saveHumorData(@Body humorData: HumorData): Response<HumorData>
}