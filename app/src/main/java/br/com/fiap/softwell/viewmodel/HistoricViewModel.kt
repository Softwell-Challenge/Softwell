package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.ThematicAverages
import br.com.fiap.softwell.model.UserHumorResponse
import br.com.fiap.softwell.service.RetrofitFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class HistoricState {
    object Idle : HistoricState()
    object Loading : HistoricState()
    object Empty : HistoricState()
    data class Error(val message: String) : HistoricState()
    data class Success(
        val averages: ThematicAverages?,
        val humorResponses: List<UserHumorResponse>
    ) : HistoricState()
}

class HistoricViewModel : ViewModel() {
    private val psychoSocialApiService = RetrofitFactory.getPsychoSocialService()
    private val humorApiService = RetrofitFactory.getMoodService()

    private val _historicState = MutableStateFlow<HistoricState>(HistoricState.Idle)
    val historicState: StateFlow<HistoricState> = _historicState.asStateFlow()

    fun fetchAveragesByDate(date: LocalDate) {
        viewModelScope.launch {
            _historicState.value = HistoricState.Loading
            val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE)

            try {
                coroutineScope {
                    val averagesDeferred = async { psychoSocialApiService.getAveragesByDate(formattedDate) }
                    val humorDeferred = async { humorApiService.getHumorHistoryByDate(formattedDate) }

                    val averagesResponse = averagesDeferred.await()
                    val humorResponse = humorDeferred.await()

                    if (averagesResponse.isSuccessful || humorResponse.isSuccessful) {
                        val averagesData = averagesResponse.body()
                        val humorData = humorResponse.body() ?: emptyList()

                        if (averagesData == null && humorData.isEmpty()) {
                            _historicState.value = HistoricState.Empty
                        } else {
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