package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.PsychoSocial
import br.com.fiap.softwell.model.ThematicAverages
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path // <-- ✅ ADICIONE ESTA IMPORTAÇÃO

interface PsychoSocialApiService {

    @POST("api/psychosocial/submit")
    suspend fun submitAnswers(
        @Body answers: PsychoSocial
    ): Response<PsychoSocial>

    @GET("api/psychosocial/analysis/averages")
    suspend fun getOverallAverages(): Response<ThematicAverages>

    @GET("/api/psychosocial/analysis/latest-averages")
    suspend fun getLatestAverages(): Response<ThematicAverages>

    @GET("/api/psychosocial/analysis/by-date/{date}")
    suspend fun getAveragesByDate(@Path("date") date: String): Response<ThematicAverages>
}
