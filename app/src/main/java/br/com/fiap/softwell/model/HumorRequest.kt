package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class HumorRequest(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("estadoDeHumor")
    val estadoDeHumor: String,

    @SerializedName("emoji")
    val emoji: String
)
