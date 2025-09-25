package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.service.HumorApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

sealed class HumorDataState {
    object Loading : HumorDataState()
    data class Success(val data: List<HumorData>) : HumorDataState()
    data class Error(val message: String) : HumorDataState()
}

class HumorViewModel : ViewModel() {

    private val _humorDataState = MutableStateFlow<HumorDataState>(HumorDataState.Loading)

    val humorDataState: StateFlow<HumorDataState> = _humorDataState.asStateFlow()
    //val humorDataState: StateFlow<HumorDataState> = _humorDataState

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL corrigida para o emulador
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val apiService = retrofit.create(HumorApiService::class.java)

    fun fetchHumorData() {
        viewModelScope.launch {
            _humorDataState.value = HumorDataState.Loading
            try {
                val response = apiService.getHumorData()
                if (response.isSuccessful) {
                    _humorDataState.value = HumorDataState.Success(response.body() ?: emptyList())
                } else {
                    _humorDataState.value = HumorDataState.Error("Erro ao carregar dados: ${response.code()}")
                }
            } catch (e: Exception) {
                _humorDataState.value = HumorDataState.Error("Exceção: ${e.message}")
            }
        }
    }

    fun saveUserResponse(estadoDeHumor: String, emoji: String) {
        viewModelScope.launch {
            try {
                // Cria um objeto HumorData com a resposta do usuário
                val userResponse = HumorData(estadoDeHumor, emoji)

                // Envia a requisição POST para o backend
                val response = apiService.saveUserResponse(userResponse)

                if (response.isSuccessful) {
                    // Log de sucesso
                    println("Resposta do usuário salva com sucesso!")
                } else {
                    // Log de erro
                    println("Erro ao salvar resposta: ${response.code()}")
                }
            } catch (e: Exception) {
                // Log de exceção
                println("Exceção ao salvar resposta: ${e.message}")
            }
        }
    }

    fun addHumor(estadoDeHumor: String, emoji: String) {
        viewModelScope.launch {
            try {
                val newHumor = HumorData(estadoDeHumor, emoji)
                val response = apiService.addHumor(newHumor)

                if (response.isSuccessful) {
                    println("Novo humor adicionado com sucesso!")
                    // Opcional: recarregar a lista para exibir o novo item
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