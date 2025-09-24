package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.service.HumorApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val humorDataState: StateFlow<HumorDataState> = _humorDataState

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL corrigida para o emulador
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val apiService = retrofit.create(HumorApiService::class.java)

    fun fetchHumorData() {
        viewModelScope.launch {
            _humorDataState.value = HumorDataState.Loading
            try {
                // Chama a API e espera por uma lista de objetos
                val response = apiService.getHumorData()
                if (response.isSuccessful) {
                    val dataList = response.body()
                    if (!dataList.isNullOrEmpty()) {
                        // Envia a lista inteira para o estado de sucesso
                        _humorDataState.value = HumorDataState.Success(dataList)
                    } else {
                        _humorDataState.value = HumorDataState.Error("Lista de dados vazia")
                    }
                } else {
                    _humorDataState.value = HumorDataState.Error("Erro na requisição: ${response.code()}")
                }
            } catch (e: Exception) {
                _humorDataState.value = HumorDataState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun saveUserResponse(estadoDeHumor: String, emoji: String) {
        viewModelScope.launch {
            try {
                // Cria um objeto HumorData com a resposta do usuário
                val userResponse = HumorData(estadoDeHumor, emoji)

                // Envia a requisição POST para o backend
                val response = apiService.saveHumorData(userResponse)

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
}