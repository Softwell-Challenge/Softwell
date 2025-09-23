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
    data class Success(val data: HumorData) : HumorDataState()
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
                val data = apiService.getHumorData()
                _humorDataState.value = HumorDataState.Success(data)
            } catch (e: Exception) {
                _humorDataState.value = HumorDataState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}