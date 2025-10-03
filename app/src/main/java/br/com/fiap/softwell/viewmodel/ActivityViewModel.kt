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

sealed class ActivityDataState {
    object Loading : ActivityDataState()
    data class Success(val activities: List<ActivityData>, val report: List<ActivityVoteReportDTO>) : ActivityDataState()
    data class Error(val message: String) : ActivityDataState()
}

class ActivityViewModel(
    private val apiService: ActivityApiService
) : ViewModel() {

    private val _activityDataState = MutableStateFlow<ActivityDataState>(ActivityDataState.Loading)
    val activityDataState: StateFlow<ActivityDataState> = _activityDataState

    private suspend fun <T> safeApiCall(apiCall: suspend () -> retrofit2.Response<T>): T? {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                return response.body()
            } else {
                val errorBody = response.errorBody()?.string() ?: "Erro HTTP ${response.code()}"
                _activityDataState.value = ActivityDataState.Error("Erro do servidor: ${response.code()} - $errorBody")
                throw HttpException(response)
            }
        } catch (e: IOException) {
            _activityDataState.value = ActivityDataState.Error("Erro de rede. Verifique sua conexão.")
        } catch (e: HttpException) {
        } catch (e: Exception) {
            _activityDataState.value = ActivityDataState.Error("Erro desconhecido: ${e.message}")
        }
        return null
    }

    fun fetchData() {
        viewModelScope.launch {
            _activityDataState.value = ActivityDataState.Loading

            val activities = safeApiCall { apiService.getActivities() }
            val report = safeApiCall { apiService.getVoteReport() }

            if (activities != null && report != null) {
                _activityDataState.value = ActivityDataState.Success(activities, report)
            }
        }
    }

    fun addActivity(activityName: String) {
        viewModelScope.launch {
            _activityDataState.value = ActivityDataState.Loading

            val newActivityDTO = ActivityCreateDTO(activity = activityName)

            val result = safeApiCall { apiService.addActivity(newActivityDTO) }

            if (result != null) {
                fetchData()
            }
        }
    }

    fun deleteActivity(id: String) {
        viewModelScope.launch {
            _activityDataState.value = ActivityDataState.Loading

            val result = safeApiCall { apiService.deleteActivity(id) }

            if (_activityDataState.value !is ActivityDataState.Error) {
                fetchData()
            }
        }
    }
}