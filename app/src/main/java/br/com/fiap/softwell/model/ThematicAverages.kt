package br.com.fiap.softwell.model

// DTO para receber as 5 médias do backend (As chaves devem ser as mesmas do JSON do Java)
data class ThematicAverages(
    val avgWorkload: Double,
    val avgWarningSigns: Double,
    val avgRelationshipClimate: Double,
    val avgCommunication: Double,
    val avgLeadershipRelation: Double
)