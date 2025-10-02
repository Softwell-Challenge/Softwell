package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.HumorData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Path

interface HumorApiService {
    @GET("api/humores")
    suspend fun getHumorData(): Response<List<HumorData>>

    @POST("api/humores/userresponse")
    suspend fun saveUserResponse(@Body humorData: HumorData): Response<HumorData>

    @POST("api/humores/add")
    suspend fun addHumor(@Body humorData: HumorData): Response<HumorData>

    @DELETE("api/humores/{id}")
    suspend fun deleteHumor(@Path("id") id: String): Response<Void>
}