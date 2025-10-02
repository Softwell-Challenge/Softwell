package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.ActivityCreateDTO
import br.com.fiap.softwell.model.ActivityData
import br.com.fiap.softwell.model.ActivityVoteReportDTO
import br.com.fiap.softwell.service.ActivityApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// ----------------------------------------------------
// Estados para gerenciar a UI
// ----------------------------------------------------
sealed class ActivityDataState {
    object Loading : ActivityDataState()
    // O relatório sempre contém a lista de atividades (activities) e o relatório de votos (report)
    data class Success(val activities: List<ActivityData>, val report: List<ActivityVoteReportDTO>) : ActivityDataState()
    data class Error(val message: String) : ActivityDataState()
}

class ActivityViewModel(
    // Injetar a instância real do seu serviço Retrofit aqui
    private val apiService: ActivityApiService
) : ViewModel() {

    private val _activityDataState = MutableStateFlow<ActivityDataState>(ActivityDataState.Loading)
    val activityDataState: StateFlow<ActivityDataState> = _activityDataState

    /**
     * Tenta buscar dados da API e trata o Response<T>.
     * Se falhar, define o estado de erro.
     * @param apiCall Função de suspensão que faz a chamada Retrofit.
     * @return O corpo da resposta (T) se for bem-sucedido, ou null.
     */
    private suspend fun <T> safeApiCall(apiCall: suspend () -> retrofit2.Response<T>): T? {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                return response.body()
            } else {
                // Erro HTTP (4xx, 5xx)
                // Se o corpo for nulo, tenta pegar o erro do código
                val errorBody = response.errorBody()?.string() ?: "Erro HTTP ${response.code()}"
                _activityDataState.value = ActivityDataState.Error("Erro do servidor: ${response.code()} - $errorBody")
                throw HttpException(response)
            }
        } catch (e: IOException) {
            // Erro de rede (sem conexão)
            _activityDataState.value = ActivityDataState.Error("Erro de rede. Verifique sua conexão.")
        } catch (e: HttpException) {
            // Este catch pega a exceção lançada no bloco 'if (response.isSuccessful)'
            // O erro já foi definido no bloco acima, mas garantimos a saída
            // Não faz nada aqui para evitar reescrever o estado de erro, apenas sai.
        } catch (e: Exception) {
            // Outros erros (Gson, serialização, etc.)
            _activityDataState.value = ActivityDataState.Error("Erro desconhecido: ${e.message}")
        }
        return null
    }

    /**
     * Busca a lista de atividades (opções de CRUD) e o relatório de votos.
     */
    fun fetchData() {
        viewModelScope.launch {
            _activityDataState.value = ActivityDataState.Loading

            // Usamos safeApiCall para buscar os dois dados
            val activities = safeApiCall { apiService.getActivities() }
            val report = safeApiCall { apiService.getVoteReport() }

            // Se ambos os dados vieram com sucesso, atualiza o estado
            if (activities != null && report != null) {
                _activityDataState.value = ActivityDataState.Success(activities, report)
            }
        }
    }

    /**
     * Adiciona uma nova opção de atividade para votação.
     */
    fun addActivity(activityName: String) {
        viewModelScope.launch {
            // Define o estado de Loading para o usuário saber que algo está acontecendo
            _activityDataState.value = ActivityDataState.Loading

            // 1. Cria o DTO LIMPO sem o ID
            val newActivityDTO = ActivityCreateDTO(activity = activityName)

            // 2. Chama a API com o novo DTO (A API retorna a ActivityData criada)
            val result = safeApiCall { apiService.addActivity(newActivityDTO) }

            // 3. Verifica o resultado
            if (result != null) {
                // Se a criação foi bem-sucedida, recarrega os dados para atualizar a lista
                fetchData()
            }
            // Se falhou, o safeApiCall já definiu o estado de erro.
        }
    }

    /**
     * Exclui uma opção de atividade pelo ID.
     */
    fun deleteActivity(id: String) {
        viewModelScope.launch {
            // Define o estado de Loading
            _activityDataState.value = ActivityDataState.Loading

            // A rota DELETE retorna Response<Void> ou Response<Unit>, tratado no safeApiCall (retorna null)
            val result = safeApiCall { apiService.deleteActivity(id) }

            // Se o safeApiCall não encontrou um erro (e result é null, o que é esperado para um DELETE 204)
            if (_activityDataState.value !is ActivityDataState.Error) {
                // Recarrega todos os dados após a exclusão para atualizar a tela
                fetchData()
            } else {
                // Se houve erro na exclusão, tenta recarregar os dados para sair do estado de Loading se falhou
                // (O safeApiCall já define o erro, mas queremos garantir que o estado de Loading não persiste)
                // Se o estado já é um erro, não precisamos chamar fetchData, ele fica no erro.
                // Mas se for uma exceção que o safeApiCall não pegou, o loading permanece.
                // Aqui vamos confiar que o safeApiCall define o estado de erro corretamente.
            }
        }
    }
}