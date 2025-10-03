package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.ActivityCreateDTO
import br.com.fiap.softwell.model.ActivityData
import br.com.fiap.softwell.model.ActivityVoteDTO
import br.com.fiap.softwell.model.ActivityVoteReportDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivityApiService {
    @GET("act/activity")
    suspend fun getActivities(): Response<List<ActivityData>>

    @POST("act/activity")
    suspend fun addActivity(@Body activity: ActivityCreateDTO): Response<ActivityData>

    @DELETE("act/activity/{id}")
    suspend fun deleteActivity(@Path("id") id: String): Response<Void>

    @GET("act/report")
    suspend fun getVoteReport(): Response<List<ActivityVoteReportDTO>>

    @POST("act/choice")
    suspend fun registerVote(@Body vote: ActivityVoteDTO): Response<ActivityVoteDTO>
}