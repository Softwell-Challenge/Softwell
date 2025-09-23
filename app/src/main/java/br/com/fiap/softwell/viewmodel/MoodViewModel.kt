package br.com.fiap.softwell.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class MoodViewModel : ViewModel() {

    private val _moods = MutableStateFlow<List<Mood>>(emptyList())
    val moods: StateFlow<List<Mood>> = _moods.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    fun fetchAllMoods() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val call = RetrofitFactory().getMoodService().getAllMood()
            call.enqueue(object : Callback<List<Mood>> {
                override fun onResponse(call: Call<List<Mood>>, response: Response<List<Mood>>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _moods.value = response.body() ?: emptyList()
                        Log.i("MoodViewModel", "onResponse: Sucesso! ${_moods.value}")
                    } else {
                        _error.value = "Erro ao buscar dados: ${response.code()}"
                        Log.e("MoodViewModel", "onResponse: Erro ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<List<Mood>>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = "Falha na conexão: ${t.message}"
                    Log.e("MoodViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }
}