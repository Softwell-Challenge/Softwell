package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class ActivityVoteReportDTO(
    @SerializedName("activityId")
    val activityId: String,

    @SerializedName("activityName")
    val activityName: String,

    @SerializedName("voteCount")
    val voteCount: Long
)