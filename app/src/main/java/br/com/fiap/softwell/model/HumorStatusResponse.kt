package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class HumorStatusResponse(
    @SerializedName("canMakeChoice")
    val canMakeChoice: Boolean,

    @SerializedName("daysRemaining")
    val timeRemainingInSeconds: Long
)
