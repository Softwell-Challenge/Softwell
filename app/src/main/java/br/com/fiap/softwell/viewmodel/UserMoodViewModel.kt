package br.com.fiap.softwell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softwell.model.UserMood
import br.com.fiap.softwell.repository.UserMoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserMoodViewModel(private val repository: UserMoodRepository) : ViewModel() {

    private val _userMoods = MutableStateFlow<List<UserMood>>(emptyList())
    val userMoods: StateFlow<List<UserMood>> = _userMoods

    // Função para inserir um novo UserMood
    fun insertUserMood(userMood: UserMood) {
        viewModelScope.launch {
            repository.insertUserMood(userMood)
            // Atualiza a lista após inserir
            loadUserMoods()
        }
    }

    // Função para carregar todos os moods
    fun loadUserMoods() {
        viewModelScope.launch {
            _userMoods.value = repository.getAllUserMoods()
        }
    }
}
