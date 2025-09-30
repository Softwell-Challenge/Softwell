package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.softwell.service.ActivityApiService

// Usa a mesma ActivityApiService porque ela tem o método registerVote()
class UserActivityViewModelFactory(
    private val apiService: ActivityApiService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Passa a dependência 'apiService' para o construtor do ViewModel
            return UserActivityViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}