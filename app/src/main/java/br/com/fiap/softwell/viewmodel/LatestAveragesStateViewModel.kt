package br.com.fiap.softwell.viewmodel

import br.com.fiap.softwell.model.ThematicAverages

sealed class LatestAveragesState {
    object Loading : LatestAveragesState()
    data class Success(val averages: ThematicAverages?) : LatestAveragesState()
    data class Error(val message: String) : LatestAveragesState()
    object Empty : LatestAveragesState() // Para quando não houver dados
}