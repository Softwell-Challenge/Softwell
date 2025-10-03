package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.model.HumorRequest // Importa o novo modelo
import br.com.fiap.softwell.model.HumorStatusResponse // Importa o novo modelo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HumorApiService {

    // Endpoint para buscar as opções de humor (mantido)
    @GET("api/humores")
    suspend fun getHumorData(): Response<List<HumorData>>

    // ✅ NOVO: Endpoint para verificar o status de envio do usuário
    @GET("api/humores/status/{userId}")
    suspend fun getHumorStatus(@Path("userId") userId: String): Response<HumorStatusResponse>

    // ✅ NOVO: Endpoint para salvar a resposta de humor do usuário
    @POST("api/humores/userhumor")
    suspend fun saveUserHumor(@Body humorRequest: HumorRequest): Response<Unit>

    // --- Endpoints antigos/de admin que podem ser mantidos ---

    @POST("api/humores/userresponse")
    suspend fun saveUserResponse(@Body humorData: HumorData): Response<HumorData>

    @POST("api/humores/add")
    suspend fun addHumor(@Body humorData: HumorData): Response<HumorData>

    @DELETE("api/humores/{id}")
    suspend fun deleteHumor(@Path("id") id: String): Response<Void>
}