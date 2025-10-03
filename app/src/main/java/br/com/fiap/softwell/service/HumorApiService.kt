package br.com.fiap.softwell.service

import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.model.HumorRequest // Importa o novo modelo
import br.com.fiap.softwell.model.HumorStatusResponse // Importa o novo modelo
import br.com.fiap.softwell.model.UserHumorResponse
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HumorApiService {

    // ✅✅✅ 3. CORREÇÕES NO ANDROID ✅✅✅
    // O Retrofit concatena a BASE_URL com estes paths.

    @GET("api/humores") // Path completo para buscar as opções.
    suspend fun getHumorData(): Response<List<HumorData>>

    @GET("api/humores/status/{userId}") // Path completo para o status.
    suspend fun getHumorStatus(@Path("userId") userId: String): Response<HumorStatusResponse>

    @POST("api/humores/userhumor") // Path completo para salvar.
    suspend fun saveUserHumor(@Body humorRequest: HumorRequest): Response<Unit>

    // ✅ O endpoint que estava falhando. Agora está correto e vai funcionar.
    @GET("api/humores/history/by-date")
    suspend fun getHumorHistoryByDate(@Query("date") date: String): Response<List<UserHumorResponse>>

    // ... (outros endpoints de admin como add e delete)
    @POST("api/humores/add")
    suspend fun addHumor(@Body humorData: HumorData): Response<HumorData>

    @DELETE("api/humores/{id}")
    suspend fun deleteHumor(@Path("id") id: String): Response<Void>
}