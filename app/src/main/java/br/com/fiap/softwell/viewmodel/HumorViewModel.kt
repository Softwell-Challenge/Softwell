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

    // Estados para controlar o bloqueio do botão de envio
    private val _isSubmissionAllowed = MutableStateFlow(true)
    val isSubmissionAllowed: StateFlow<Boolean> = _isSubmissionAllowed.asStateFlow()

    private val _submissionStatusText = MutableStateFlow("ENVIAR")
    val submissionStatusText: StateFlow<String> = _submissionStatusText.asStateFlow()

    // Usa a RetrofitFactory centralizada que já tem o interceptor
    private val apiService = RetrofitFactory.getMoodService()

    init {
        fetchHumorData()
    }

    // Função para verificar o status no backend
    fun checkSubmissionStatus() {
        // Pega o ID do usuário do token salvo
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
                        // Formata o tempo restante de segundos para horas e minutos
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

    // ✅ CORREÇÃO: Este método agora substitui o antigo 'saveUserResponse'.
    // Função para salvar o humor usando o novo endpoint.
    fun saveUserResponse(estadoDeHumor: String, emoji: String) {
        val userId = AuthTokenManager.getUserIdFromToken() ?: return

        viewModelScope.launch {
            try {
                val humorRequest = HumorRequest(userId, estadoDeHumor, emoji)
                val response = apiService.saveUserHumor(humorRequest)

                if (response.isSuccessful) {
                    Log.i("HumorViewModel", "Resposta de humor salva com sucesso!")
                    // Após salvar, atualiza o status para bloquear o botão novamente
                    checkSubmissionStatus()
                } else {
                    // Trata o erro de cooldown que vem do backend
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

    // Função original para buscar as opções de humor (sem alterações de lógica).
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
        // NOVO CALLBACK: para notificar a UI se o limite for atingido
        onLimitReached: () -> Unit
    ) {
        // 1. Tenta obter a lista atual de humores do estado de sucesso
        val currentHumors = (_humorDataState.value as? HumorDataState.Success)?.data ?: emptyList()

        // 2. VERIFICAÇÃO DA LÓGICA DE LIMITE (9 humores)
        if (currentHumors.size >= 9) {
            onLimitReached() // Chama o callback para mostrar a mensagem na tela
            return // Para a execução, impedindo a chamada da API
        }

        viewModelScope.launch {
            try {
                val newHumor = HumorData(estadoDeHumor, emoji)
                val response = apiService.addHumor(newHumor)

                if (response.isSuccessful) {
                    println("Novo humor adicionado com sucesso!")
                    // Recarregar a lista para exibir o novo item
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
                    // Recarrega a lista para refletir a exclusão
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




//package br.com.fiap.softwell.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import br.com.fiap.softwell.model.HumorData
//import br.com.fiap.softwell.service.HumorApiService
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import retrofit2.Retrofit
//import retrofit2.converter.moshi.MoshiConverterFactory
//
//sealed class HumorDataState {
//    object Loading : HumorDataState()
//    data class Success(val data: List<HumorData>) : HumorDataState()
//    data class Error(val message: String) : HumorDataState()
//}
//
//class HumorViewModel : ViewModel() {
//
//    private val _humorDataState = MutableStateFlow<HumorDataState>(HumorDataState.Loading)
//
//    val humorDataState: StateFlow<HumorDataState> = _humorDataState.asStateFlow()
//    //val humorDataState: StateFlow<HumorDataState> = _humorDataState
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl("http://10.0.2.2:8080/") // URL corrigida para o emulador
//        .addConverterFactory(MoshiConverterFactory.create())
//        .build()
//
//    private val apiService = retrofit.create(HumorApiService::class.java)
//
//    fun fetchHumorData() {
//        viewModelScope.launch {
//            _humorDataState.value = HumorDataState.Loading
//            try {
//                val response = apiService.getHumorData()
//                if (response.isSuccessful) {
//                    _humorDataState.value = HumorDataState.Success(response.body() ?: emptyList())
//                } else {
//                    _humorDataState.value = HumorDataState.Error("Erro ao carregar dados: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                _humorDataState.value = HumorDataState.Error("Exceção: ${e.message}")
//            }
//        }
//    }
//
//    fun saveUserResponse(estadoDeHumor: String, emoji: String) {
//        viewModelScope.launch {
//            try {
//                // Cria um objeto HumorData com a resposta do usuário
//                val userResponse = HumorData(estadoDeHumor, emoji)
//
//                // Envia a requisição POST para o backend
//                val response = apiService.saveUserResponse(userResponse)
//
//                if (response.isSuccessful) {
//                    // Log de sucesso
//                    println("Resposta do usuário salva com sucesso!")
//                } else {
//                    // Log de erro
//                    println("Erro ao salvar resposta: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                // Log de exceção
//                println("Exceção ao salvar resposta: ${e.message}")
//            }
//        }
//    }
//
//    fun addHumor(
//        estadoDeHumor: String,
//        emoji: String,
//        // NOVO CALLBACK: para notificar a UI se o limite for atingido
//        onLimitReached: () -> Unit
//    ) {
//        // 1. Tenta obter a lista atual de humores do estado de sucesso
//        val currentHumors = (_humorDataState.value as? HumorDataState.Success)?.data ?: emptyList()
//
//        // 2. VERIFICAÇÃO DA LÓGICA DE LIMITE (9 humores)
//        if (currentHumors.size >= 9) {
//            onLimitReached() // Chama o callback para mostrar a mensagem na tela
//            return // Para a execução, impedindo a chamada da API
//        }
//
//        viewModelScope.launch {
//            try {
//                val newHumor = HumorData(estadoDeHumor, emoji)
//                val response = apiService.addHumor(newHumor)
//
//                if (response.isSuccessful) {
//                    println("Novo humor adicionado com sucesso!")
//                    // Recarregar a lista para exibir o novo item
//                    fetchHumorData()
//                } else {
//                    println("Erro ao adicionar humor: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                println("Exceção ao adicionar humor: ${e.message}")
//            }
//        }
//    }
//
//    fun deleteHumor(id: String) {
//        viewModelScope.launch {
//            try {
//                val response = apiService.deleteHumor(id)
//                if (response.isSuccessful) {
//                    println("Humor excluído com sucesso!")
//                    // Recarrega a lista para refletir a exclusão
//                    fetchHumorData()
//                } else {
//                    println("Erro ao excluir humor: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                println("Exceção ao excluir humor: ${e.message}")
//            }
//        }
//    }
//}