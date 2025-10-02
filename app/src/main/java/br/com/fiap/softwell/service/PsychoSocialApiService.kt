package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.PsychoSocial
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PsychoSocialApiService {

    @POST("api/psychosocial/submit")
    suspend fun submitAnswers(
        @Body answers: PsychoSocial
    ): Response<PsychoSocial>
}