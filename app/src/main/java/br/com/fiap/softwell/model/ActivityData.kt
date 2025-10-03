package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class ActivityData(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("activity")
    val activity: String,
)