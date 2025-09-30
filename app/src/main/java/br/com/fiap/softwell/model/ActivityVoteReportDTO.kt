package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class ActivityVoteReportDTO(
    // ID da Atividade, que é o ObjectId do MongoDB
    @SerializedName("activityId")
    val activityId: String,

    @SerializedName("activityName")
    val activityName: String,

    @SerializedName("voteCount")
    val voteCount: Long
)