package br.com.fiap.softwell.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.ActivityData
import br.com.fiap.softwell.model.ActivityVoteDTO
import br.com.fiap.softwell.service.ActivityApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// ----------------------------------------------------
// Estados para gerenciar a UI do Usuário
// ----------------------------------------------------
sealed class UserActivityState {
    object Loading : UserActivityState()
    data class Success(val activities: List<ActivityData>) : UserActivityState()
    data class Error(val message: String) : UserActivityState()
    object VoteRegistered : UserActivityState() // NOVO: Estado para notificar voto
}

class UserActivityViewModel(
    // A CORREÇÃO PRINCIPAL: Receber o serviço via construtor
    private val apiService: ActivityApiService
) : ViewModel() {

    private val _activityState = MutableStateFlow<UserActivityState>(UserActivityState.Loading)
    val activityState: StateFlow<UserActivityState> = _activityState.asStateFlow()

    // ----------------------------------------------------
    // FUNÇÕES DE DADOS (Listar Opções)
    // ----------------------------------------------------
    fun fetchActivities() {
        viewModelScope.launch {
            _activityState.value = UserActivityState.Loading
            try {
                // Chama a API para listar as opções de votação
                val response = apiService.getActivities()

                if (response.isSuccessful) {
                    _activityState.value = UserActivityState.Success(response.body() ?: emptyList())
                } else {
                    _activityState.value = UserActivityState.Error("Erro HTTP: ${response.code()}")
                }
            } catch (e: IOException) {
                _activityState.value = UserActivityState.Error("Erro de rede. Verifique sua conexão.")
            } catch (e: Exception) {
                _activityState.value = UserActivityState.Error("Erro ao buscar atividades: ${e.message}")
            }
        }
    }

    // ----------------------------------------------------
    // FUNÇÃO DE VOTAÇÃO (Seu Código Corrigido)
    // ----------------------------------------------------
    fun registrarVoto(activityId: String) {
        viewModelScope.launch {
            try {
                // 1. Embalar o dado (Criar o DTO)
                val voteDTO = ActivityVoteDTO(activityId = activityId)

                // 2. Chamar a API (Agora 'apiService' está disponível)
                val response = apiService.registerVote(voteDTO)

                if (response.isSuccessful) {
                    // 3. Notificar sucesso e redefinir o estado para evitar re-votação imediata
                    _activityState.value = UserActivityState.VoteRegistered
                    Log.d("Voto", "Voto registrado com sucesso para o ID: $activityId")
                } else {
                    _activityState.value = UserActivityState.Error("Falha ao votar: Código ${response.code()}")
                    Log.e("Voto", "Erro ao registrar voto: ${response.code()}")
                }
            } catch (e: IOException) {
                _activityState.value = UserActivityState.Error("Falha na conexão ao registrar voto.")
                Log.e("Voto", "Falha de rede: ${e.message}")
            } catch (e: Exception) {
                _activityState.value = UserActivityState.Error("Erro desconhecido ao votar: ${e.message}")
            }
        }
    }

    // Método para redefinir o estado de VoteRegistered após exibição
    fun resetVoteState() {
        // Redefine para o estado de sucesso anterior (mantendo a lista) ou para Loading
        val currentState = _activityState.value
        if (currentState is UserActivityState.VoteRegistered) {
            // Tenta manter a lista atual para não piscar a tela
            fetchActivities()
        }
    }
}