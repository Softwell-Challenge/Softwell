package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.softwell.service.ActivityApiService

class UserActivityViewModelFactory(
    private val apiService: ActivityApiService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserActivityViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}