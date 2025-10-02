package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.PsychoSocial
import br.com.fiap.softwell.model.ThematicAverages
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PsychoSocialApiService {

    @POST("api/psychosocial/submit")
    suspend fun submitAnswers(
        @Body answers: PsychoSocial
    ): Response<PsychoSocial>

    // ✅ Endpoint para buscar as 5 médias
    @GET("api/psychosocial/analysis/averages")
    suspend fun getOverallAverages(): Response<ThematicAverages>

    @GET("/api/psychosocial/analysis/latest-averages")
    suspend fun getLatestAverages(): Response<ThematicAverages>
}