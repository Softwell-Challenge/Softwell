package br.com.fiap.softwell.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoodOption(
    val id: String,
    @Json(name = "text") val label: String
)