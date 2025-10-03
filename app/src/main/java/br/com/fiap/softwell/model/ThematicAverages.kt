package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

// Este data class representa a resposta JSON da sua API de médias.
// O Retrofit usará isso para converter a resposta em um objeto Kotlin.
data class ThematicAverages(

    // O @SerializedName garante que, mesmo que o nome da variável no Kotlin
    // seja diferente, ele será mapeado corretamente a partir do JSON.
    // Use os nomes EXATOS que seu backend retorna.

    @SerializedName("workloadAverage")
    val workloadAverage: Double,

    @SerializedName("warningSignsAverage")
    val warningSignsAverage: Double,

    @SerializedName("relationshipClimateAverage")
    val relationshipClimateAverage: Double,

    @SerializedName("communicationAverage")
    val communicationAverage: Double,

    @SerializedName("leadershipRelationAverage")
    val leadershipRelationAverage: Double
)
