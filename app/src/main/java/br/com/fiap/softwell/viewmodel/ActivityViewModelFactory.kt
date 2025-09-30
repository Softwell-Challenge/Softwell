package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.softwell.service.ActivityApiService // Importa a interface do serviço de API

// Esta classe é responsável por criar o ActivityViewModel
// injetando a dependência ActivityApiService no construtor.
class ActivityViewModelFactory(
    private val apiService: ActivityApiService
) : ViewModelProvider.Factory {

    // Esta função é chamada automaticamente pelo Android/Compose
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se a classe que estamos tentando criar é o ActivityViewModel
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
            // Se for, cria uma nova instância passando o serviço de API real
            @Suppress("UNCHECKED_CAST")
            return ActivityViewModel(apiService) as T
        }

        // Se for outra classe, lança um erro
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}