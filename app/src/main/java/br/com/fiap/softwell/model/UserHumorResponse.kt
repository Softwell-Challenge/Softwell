package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

// Representa a entidade UserHumorResponse do backend
data class UserHumorResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("userId") val userId: String,
    @SerializedName("estadoDeHumor") val estadoDeHumor: String,
    @SerializedName("emoji") val emoji: String,
    // O backend envia LocalDateTime, o Gson/Retrofit pode converter para String
    @SerializedName("dataResposta") val dataResposta: String
)
