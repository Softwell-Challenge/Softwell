package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.model.HumorRequest
import br.com.fiap.softwell.model.HumorStatusResponse
import br.com.fiap.softwell.model.UserHumorResponse
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HumorApiService {

    @GET("api/humores")
    suspend fun getHumorData(): Response<List<HumorData>>

    @GET("api/humores/status/{userId}")
    suspend fun getHumorStatus(@Path("userId") userId: String): Response<HumorStatusResponse>

    @POST("api/humores/userhumor")
    suspend fun saveUserHumor(@Body humorRequest: HumorRequest): Response<Unit>

    @GET("api/humores/history/by-date")
    suspend fun getHumorHistoryByDate(@Query("date") date: String): Response<List<UserHumorResponse>>

    @POST("api/humores/add")
    suspend fun addHumor(@Body humorData: HumorData): Response<HumorData>

    @DELETE("api/humores/{id}")
    suspend fun deleteHumor(@Path("id") id: String): Response<Void>
}