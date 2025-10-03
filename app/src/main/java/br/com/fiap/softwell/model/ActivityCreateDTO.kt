package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class ActivityCreateDTO(
    @SerializedName("activity")
    val activity: String,
)