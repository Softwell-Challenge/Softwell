package br.com.fiap.softwell.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.model.HumorRequest
import br.com.fiap.softwell.service.AuthTokenManager
import br.com.fiap.softwell.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

sealed class HumorDataState {
    object Loading : HumorDataState()
    data class Success(val data: List<HumorData>) : HumorDataState()
    data class Error(val message: String) : HumorDataState()
}

class HumorViewModel : ViewModel() {
    private val _humorDataState = MutableStateFlow<HumorDataState>(HumorDataState.Loading)
    val humorDataState: StateFlow<HumorDataState> = _humorDataState.asStateFlow()

    private val _isSubmissionAllowed = MutableStateFlow(true)
    val isSubmissionAllowed: StateFlow<Boolean> = _isSubmissionAllowed.asStateFlow()

    private val _submissionStatusText = MutableStateFlow("ENVIAR")
    val submissionStatusText: StateFlow<String> = _submissionStatusText.asStateFlow()

    private val apiService = RetrofitFactory.getMoodService()

    init {
        fetchHumorData()
    }

    fun checkSubmissionStatus() {
        val userId = AuthTokenManager.getUserIdFromToken()
        if (userId == null) {
            _submissionStatusText.value = "Usuário não logado"
            _isSubmissionAllowed.value = false
            return
        }

        viewModelScope.launch {
            try {
                val response = apiService.getHumorStatus(userId)
                if (response.isSuccessful) {
                    val status = response.body()
                    _isSubmissionAllowed.value = status?.canMakeChoice ?: false
                    if (status?.canMakeChoice == false) {
                        val seconds = status.timeRemainingInSeconds
                        val hours = TimeUnit.SECONDS.toHours(seconds)
                        val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
                        _submissionStatusText.value = "Aguarde ${hours}h ${minutes}m"
                    } else {
                        _submissionStatusText.value = "ENVIAR"
                    }
                } else {
                    _submissionStatusText.value = "Erro ao verificar"
                    _isSubmissionAllowed.value = false
                }
            } catch (e: Exception) {
                _submissionStatusText.value = "Erro de conexão"
                _isSubmissionAllowed.value = false
                Log.e("HumorViewModel", "Falha ao verificar status: ${e.message}")
            }
        }
    }

    fun saveUserResponse(estadoDeHumor: String, emoji: String) {
        val userId = AuthTokenManager.getUserIdFromToken() ?: return

        viewModelScope.launch {
            try {
                val humorRequest = HumorRequest(userId, estadoDeHumor, emoji)
                val response = apiService.saveUserHumor(humorRequest)

                if (response.isSuccessful) {
                    Log.i("HumorViewModel", "Resposta de humor salva com sucesso!")
                    checkSubmissionStatus()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _submissionStatusText.value = errorBody ?: "Aguarde para enviar novamente."
                    _isSubmissionAllowed.value = false
                    Log.e("HumorViewModel", "Erro ao salvar: $errorBody")
                }
            } catch (e: Exception) {
                _submissionStatusText.value = "Erro de conexão"
                _isSubmissionAllowed.value = false
                Log.e("HumorViewModel", "Exceção ao salvar: ${e.message}")
            }
        }
    }

    fun fetchHumorData() {
        viewModelScope.launch {
            _humorDataState.value = HumorDataState.Loading
            try {
                val response = apiService.getHumorData()
                if (response.isSuccessful) {
                    _humorDataState.value = HumorDataState.Success(response.body() ?: emptyList())
                } else {
                    _humorDataState.value = HumorDataState.Error("Erro: ${response.code()}")
                }
            } catch (e: Exception) {
                _humorDataState.value = HumorDataState.Error("Exceção: ${e.message}")
            }
        }
    }

    fun addHumor(
        estadoDeHumor: String,
        emoji: String,
        onLimitReached: () -> Unit
    ) {
        val currentHumors = (_humorDataState.value as? HumorDataState.Success)?.data ?: emptyList()

        if (currentHumors.size >= 9) {
            onLimitReached()
            return
        }

        viewModelScope.launch {
            try {
                val newHumor = HumorData(estadoDeHumor, emoji)
                val response = apiService.addHumor(newHumor)

                if (response.isSuccessful) {
                    println("Novo humor adicionado com sucesso!")
                    fetchHumorData()
                } else {
                    println("Erro ao adicionar humor: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Exceção ao adicionar humor: ${e.message}")
            }
        }
    }

    fun deleteHumor(id: String) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteHumor(id)
                if (response.isSuccessful) {
                    println("Humor excluído com sucesso!")
                    fetchHumorData()
                } else {
                    println("Erro ao excluir humor: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Exceção ao excluir humor: ${e.message}")
            }
        }
    }
}