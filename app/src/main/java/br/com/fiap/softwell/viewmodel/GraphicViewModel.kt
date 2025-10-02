package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.service.PsychoSocialApiService
import br.com.fiap.softwell.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Alias para clareza (Map<String, Double> representa as médias)
typealias ThematicAverages = Map<String, Double>

class GraphicViewModel : ViewModel() {

    // Inicializa o serviço da API
    private val apiService: PsychoSocialApiService? = try {
        RetrofitFactory.getPsychoSocialService()
    } catch (e: Exception) {
        println("Erro fatal ao inicializar o Retrofit para PsychoSocialService: ${e.message}")
        null
    }

    // Estado para armazenar as médias (Map<String, Double>)
    private val _averages = MutableStateFlow<ThematicAverages?>(null)
    val averages: StateFlow<ThematicAverages?> = _averages

    // Estado de carregamento
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estado de erro (inclui mensagens de "não encontrado")
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        if (apiService != null) {
            fetchLatestAverages()
        } else {
            _error.value = "Erro de inicialização da API (Retrofit Factory)."
            _isLoading.value = false
        }
    }

    /**
     * Busca as 5 médias temáticas APENAS do último questionário respondido pelo usuário.
     */
    fun fetchLatestAverages() {
        if (apiService == null) return

        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                // ✅ CHAMA O ENDPOINT CORRETO
                val response = apiService.getLatestAverages()

                if (response.isSuccessful) {
                    val averagesMap = response.body() as? ThematicAverages

                    // Map<String, Double>?.isNullOrEmpty() funciona corretamente no Kotlin
                    if (averagesMap.isNullOrEmpty()) {
                        _averages.value = null
                        _error.value = "Nenhuma média psicossocial encontrada para este usuário."
                    } else {
                        // Não é necessário o cast 'as ThematicAverages?', pois o tipo já é garantido
                        _averages.value = averagesMap
                    }
                } else if (response.code() == 204) {
                    // Trata o 204 No Content (retorno esperado quando o usuário não tem questionário)
                    _averages.value = null
                    _error.value = "Nenhuma média psicossocial encontrada para este usuário."
                } else if (response.code() == 401 || response.code() == 403) {
                    _error.value = "Acesso negado. O token de autenticação pode estar ausente ou expirado."
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                    _error.value = "Falha HTTP: ${response.code()}. Detalhe: $errorBody"
                }
            } catch (e: IOException) {
                _error.value = "Falha de Conexão/Rede: ${e.message}. Verifique a URL do backend (10.0.2.2)."
            } catch (e: HttpException) {
                _error.value = "Erro inesperado na API: ${e.message}"
            } catch (e: Exception) {
                _error.value = "Erro geral: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ FUNÇÃO TODO FOI REMOVIDA DAQUI
}