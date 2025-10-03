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
import java.io.IOException


sealed class VoteFeedbackState {
    object Idle : VoteFeedbackState()
    object VotedSuccessfully : VoteFeedbackState()
    data class VoteLimitReached(val message: String) : VoteFeedbackState()
    data class Error(val message: String) : VoteFeedbackState()
}

sealed class UserActivityState {
    object Loading : UserActivityState()
    data class Success(val activities: List<ActivityData>) : UserActivityState()
    data class Error(val message: String) : UserActivityState()
}

class UserActivityViewModel(
    private val apiService: ActivityApiService
) : ViewModel() {

    private val _activityState = MutableStateFlow<UserActivityState>(UserActivityState.Loading)
    val activityState: StateFlow<UserActivityState> = _activityState.asStateFlow()

    private val _voteFeedbackState = MutableStateFlow<VoteFeedbackState>(VoteFeedbackState.Idle)
    val voteFeedbackState: StateFlow<VoteFeedbackState> = _voteFeedbackState.asStateFlow()

    fun fetchActivities() {
        viewModelScope.launch {
            _activityState.value = UserActivityState.Loading
            try {
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

    fun registrarVoto(activityId: String) {
        viewModelScope.launch {
            try {
                val voteDTO = ActivityVoteDTO(activityId = activityId)
                val response = apiService.registerVote(voteDTO)

                if (response.isSuccessful) {
                    _voteFeedbackState.value = VoteFeedbackState.VotedSuccessfully
                    Log.d("Voto", "Voto registrado com sucesso para o ID: $activityId")

                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"

                    if (errorCode == 400 && errorBody.contains("Você só pode fazer uma nova escolha daqui a")) {
                        _voteFeedbackState.value = VoteFeedbackState.VoteLimitReached(errorBody)
                        Log.w("Voto", "Voto bloqueado por cooldown: $errorBody")
                    } else {
                        _voteFeedbackState.value = VoteFeedbackState.Error("Erro HTTP $errorCode: Falha ao registrar voto.")
                        Log.e("Voto", "Erro HTTP: $errorCode - $errorBody")
                    }
                }

            } catch (e: IOException) {
                _voteFeedbackState.value = VoteFeedbackState.Error("Falha na conexão ao registrar voto.")
                Log.e("Voto", "Falha de rede: ${e.message}", e)
            } catch (e: Exception) {
                _voteFeedbackState.value = VoteFeedbackState.Error("Erro desconhecido ao votar: ${e.message}")
                Log.e("Voto", "Erro desconhecido: ${e.message}", e)
            } finally {
                fetchActivities()
            }
        }
    }

    fun resetVoteState() {
        _voteFeedbackState.value = VoteFeedbackState.Idle
    }
}