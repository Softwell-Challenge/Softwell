package br.com.fiap.softwell.viewmodel // O pacote mudou de 'model' para 'viewmodel'

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.Mood // Certifique-se de que Mood está importado corretamente
import br.com.fiap.softwell.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MoodViewModel : ViewModel() {

    private val _moods = MutableStateFlow<List<Mood>>(emptyList())
    val moods: StateFlow<List<Mood>> = _moods.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 1. Corrija o RetrofitFactory para ser um 'object' (Singleton)
    private val moodApiService = RetrofitFactory.getMoodService()

    fun fetchAllMoods() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // 2. Chama a API de forma síncrona e segura usando 'suspend'
                //    e tratando a resposta do Retrofit (Response<T>)
                val response = moodApiService.getAllMood()

                if (response.isSuccessful) {
                    _moods.value = response.body() ?: emptyList()
                    Log.i("MoodViewModel", "Sucesso ao buscar humores: ${_moods.value.size}")
                } else {
                    // Trata erros HTTP (4xx, 5xx)
                    _error.value = "Erro HTTP: ${response.code()}"
                    Log.e("MoodViewModel", "Erro HTTP: ${response.code()}")
                }
            } catch (e: IOException) {
                // Trata erro de rede (falha de conexão)
                _error.value = "Falha de conexão: Verifique a internet."
                Log.e("MoodViewModel", "Falha de conexão: ${e.message}")
            } catch (e: HttpException) {
                // Trata exceções de resposta do servidor
                _error.value = "Erro do servidor: ${e.code()}"
                Log.e("MoodViewModel", "Erro do servidor: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

//package br.com.fiap.softwell.model
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import br.com.fiap.softwell.service.RetrofitFactory
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//class MoodViewModel : ViewModel() {
//
//    private val _moods = MutableStateFlow<List<Mood>>(emptyList())
//    val moods: StateFlow<List<Mood>> = _moods.asStateFlow()
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> = _error.asStateFlow()
//    fun fetchAllMoods() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _error.value = null
//            val call = RetrofitFactory().getMoodService().getAllMood()
//            call.enqueue(object : Callback<List<Mood>> {
//                override fun onResponse(call: Call<List<Mood>>, response: Response<List<Mood>>) {
//                    _isLoading.value = false
//                    if (response.isSuccessful) {
//                        _moods.value = response.body() ?: emptyList()
//                        Log.i("MoodViewModel", "onResponse: Sucesso! ${_moods.value}")
//                    } else {
//                        _error.value = "Erro ao buscar dados: ${response.code()}"
//                        Log.e("MoodViewModel", "onResponse: Erro ${response.code()}")
//                    }
//                }
//                override fun onFailure(call: Call<List<Mood>>, t: Throwable) {
//                    _isLoading.value = false
//                    _error.value = "Falha na conexão: ${t.message}"
//                    Log.e("MoodViewModel", "onFailure: ${t.message}")
//                }
//            })
//        }
//    }
//}