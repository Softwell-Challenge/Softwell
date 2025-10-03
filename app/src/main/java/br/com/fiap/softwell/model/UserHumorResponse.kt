package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class UserHumorResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("userId") val userId: String,
    @SerializedName("estadoDeHumor") val estadoDeHumor: String,
    @SerializedName("emoji") val emoji: String,
    @SerializedName("dataResposta") val dataResposta: String
)
