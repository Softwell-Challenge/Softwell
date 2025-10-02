package br.com.fiap.softwell.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HumorData(
    @Json(name = "estadoDeHumor") val estadoDeHumor: String,
    @Json(name = "emoji") val emoji: String,

    // O CAMPO DO MONGODB É "_id", mas a anotação @Json o mapeia para o campo 'id' do Kotlin.
    // Ele deve ser opcional (String? = null) e ser o último na ordem.
    @Json(name = "id") val id: String? = null
)