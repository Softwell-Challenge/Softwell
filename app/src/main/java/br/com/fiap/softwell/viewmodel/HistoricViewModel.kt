package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.ThematicAverages
import br.com.fiap.softwell.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Representa os diferentes estados da tela de histórico
sealed class HistoricState {
    object Idle : HistoricState()
    object Loading : HistoricState()
    data class Success(val averages: ThematicAverages) : HistoricState()
    data class Error(val message: String) : HistoricState()
    object Empty : HistoricState()
}

class HistoricViewModel : ViewModel() {

    private val apiService by lazy { RetrofitFactory.getPsychoSocialService() }

    private val _historicState = MutableStateFlow<HistoricState>(HistoricState.Idle)
    val historicState = _historicState.asStateFlow()

    fun fetchAveragesByDate(date: LocalDate) {
        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE)

        _historicState.value = HistoricState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.getAveragesByDate(formattedDate)

                if (response.isSuccessful) {
                    val averages = response.body()
                    if (averages != null) {
                        _historicState.value = HistoricState.Success(averages)
                    } else {
                        _historicState.value = HistoricState.Empty
                    }
                } else if (response.code() == 204 || response.code() == 404) {
                    _historicState.value = HistoricState.Empty
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                    _historicState.value = HistoricState.Error("Falha na API: Cód ${response.code()} - $errorBody")
                }
            } catch (e: IOException) {
                _historicState.value = HistoricState.Error("Falha de conexão. Verifique a internet e a URL da API.")
            } catch (e: HttpException) {
                _historicState.value = HistoricState.Error("Erro HTTP: ${e.message}")
            } catch (e: Exception) {
                _historicState.value = HistoricState.Error("Erro inesperado: ${e.message}")
            }
        }
    }
}
