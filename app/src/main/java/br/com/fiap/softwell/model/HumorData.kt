package br.com.fiap.softwell.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HumorData(
    @Json(name = "questionText") val questionText: String,
    @Json(name = "moodOptions") val moodOptions: List<MoodOption>,
    @Json(name = "emojis") val emojis: List<String>
)