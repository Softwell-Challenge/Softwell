package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class ThematicAverages(
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
