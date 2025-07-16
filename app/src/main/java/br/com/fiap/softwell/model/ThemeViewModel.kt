package br.com.fiap.softwell.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    val isDarkTheme = mutableStateOf(true)

    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }
}
