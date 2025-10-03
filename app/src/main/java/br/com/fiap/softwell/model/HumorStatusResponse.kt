package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

// Corresponde ao HumorStatusResponseDTO do seu backend
data class HumorStatusResponse(
    @SerializedName("canMakeChoice")
    val canMakeChoice: Boolean,

    @SerializedName("daysRemaining")
    val timeRemainingInSeconds: Long
)
