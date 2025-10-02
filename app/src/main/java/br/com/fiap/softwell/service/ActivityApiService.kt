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

    // GET /act/activity
    // Lista todas as opções de atividade para CRUD (admin) ou votação (usuário).
    @GET("act/activity")
    suspend fun getActivities(): Response<List<ActivityData>>

    // POST /act/activity
    // Adiciona uma nova opção de atividade (Admin CRUD).
    @POST("act/activity")
    suspend fun addActivity(@Body activity: ActivityCreateDTO): Response<ActivityData>

    // DELETE /act/activity/{id}
    // Exclui uma opção de atividade pelo ID do MongoDB (Admin CRUD).
    @DELETE("act/activity/{id}")
    suspend fun deleteActivity(@Path("id") id: String): Response<Void>

    // GET /act/report
    // Obtém o relatório de votos para o Admin, com a contagem total de votos por atividade.
    @GET("act/report")
    suspend fun getVoteReport(): Response<List<ActivityVoteReportDTO>>

    // --- NOVO: REGISTRO DE VOTO DO USUÁRIO ---

    // POST /act/vote
    // Registra o voto do usuário na segunda collection (vote_record).
    @POST("act/choice")
    suspend fun registerVote(@Body vote: ActivityVoteDTO): Response<ActivityVoteDTO>
    // Retorna Response<Void> ou Response<ActivityVoteDTO> se o servidor retornar algo.
}