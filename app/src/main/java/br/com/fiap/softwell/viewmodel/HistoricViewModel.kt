package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.ThematicAverages
import br.com.fiap.softwell.model.UserHumorResponse // Importa o novo modelo
import br.com.fiap.softwell.service.RetrofitFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// O estado de sucesso agora contém as duas listas (uma delas pode ser nula/vazia)
sealed class HistoricState {
    object Idle : HistoricState()
    object Loading : HistoricState()
    object Empty : HistoricState() // Quando ambas as chamadas não retornam nada
    data class Error(val message: String) : HistoricState()
    data class Success(
        val averages: ThematicAverages?, // Dados do questionário
        val humorResponses: List<UserHumorResponse> // Dados de humor
    ) : HistoricState()
}

class HistoricViewModel : ViewModel() {
    // ✅ 1. OBTÉM OS DOIS SERVIÇOS NECESSÁRIOS
    private val psychoSocialApiService = RetrofitFactory.getPsychoSocialService()
    private val humorApiService = RetrofitFactory.getMoodService() // Renomeado para getMoodService na sua factory

    private val _historicState = MutableStateFlow<HistoricState>(HistoricState.Idle)
    val historicState: StateFlow<HistoricState> = _historicState.asStateFlow()

    // ✅ 2. A FUNÇÃO DE BUSCA FOI ATUALIZADA
    fun fetchAveragesByDate(date: LocalDate) {
        viewModelScope.launch {
            _historicState.value = HistoricState.Loading
            val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE)

            try {
                // Executa ambas as chamadas de API em paralelo para mais eficiência
                coroutineScope {
                    val averagesDeferred = async { psychoSocialApiService.getAveragesByDate(formattedDate) }
                    val humorDeferred = async { humorApiService.getHumorHistoryByDate(formattedDate) }

                    // Espera a conclusão de ambas
                    val averagesResponse = averagesDeferred.await()
                    val humorResponse = humorDeferred.await()

                    // Processa os resultados
                    if (averagesResponse.isSuccessful || humorResponse.isSuccessful) {
                        val averagesData = averagesResponse.body()
                        val humorData = humorResponse.body() ?: emptyList()

                        // Se ambas as respostas não trouxeram dados, o estado é "Vazio"
                        if (averagesData == null && humorData.isEmpty()) {
                            _historicState.value = HistoricState.Empty
                        } else {
                            // Caso contrário, é "Sucesso" com os dados obtidos
                            _historicState.value = HistoricState.Success(averagesData, humorData)
                        }
                    } else {
                        _historicState.value = HistoricState.Error("Falha na resposta do servidor")
                    }
                }
            } catch (e: Exception) {
                _historicState.value = HistoricState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}



//package br.com.fiap.softwell.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import br.com.fiap.softwell.model.ThematicAverages
//import br.com.fiap.softwell.service.RetrofitFactory
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//import java.io.IOException
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//
//// Representa os diferentes estados da tela de histórico
//sealed class HistoricState {
//    object Idle : HistoricState()
//    object Loading : HistoricState()
//    data class Success(val averages: ThematicAverages) : HistoricState()
//    data class Error(val message: String) : HistoricState()
//    object Empty : HistoricState()
//}
//
//class HistoricViewModel : ViewModel() {
//
//    private val apiService by lazy { RetrofitFactory.getPsychoSocialService() }
//
//    private val _historicState = MutableStateFlow<HistoricState>(HistoricState.Idle)
//    val historicState = _historicState.asStateFlow()
//
//    fun fetchAveragesByDate(date: LocalDate) {
//        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
//
//        _historicState.value = HistoricState.Loading
//        viewModelScope.launch {
//            try {
//                val response = apiService.getAveragesByDate(formattedDate)
//
//                if (response.isSuccessful) {
//                    val averages = response.body()
//                    if (averages != null) {
//                        _historicState.value = HistoricState.Success(averages)
//                    } else {
//                        _historicState.value = HistoricState.Empty
//                    }
//                } else if (response.code() == 204 || response.code() == 404) {
//                    _historicState.value = HistoricState.Empty
//                } else {
//                    val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
//                    _historicState.value = HistoricState.Error("Falha na API: Cód ${response.code()} - $errorBody")
//                }
//            } catch (e: IOException) {
//                _historicState.value = HistoricState.Error("Falha de conexão. Verifique a internet e a URL da API.")
//            } catch (e: HttpException) {
//                _historicState.value = HistoricState.Error("Erro HTTP: ${e.message}")
//            } catch (e: Exception) {
//                _historicState.value = HistoricState.Error("Erro inesperado: ${e.message}")
//            }
//        }
//    }
//}
