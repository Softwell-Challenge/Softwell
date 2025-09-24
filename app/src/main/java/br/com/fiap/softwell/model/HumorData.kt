package br.com.fiap.softwell.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HumorData(
    @Json(name = "estadoDeHumor") val estadoDeHumor: String,
    @Json(name = "emoji") val emoji: String
)