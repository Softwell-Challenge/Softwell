package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class ActivityVoteDTO(
    @SerializedName("activityId")
    val activityId: String
)