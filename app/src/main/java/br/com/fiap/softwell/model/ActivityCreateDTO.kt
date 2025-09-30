package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

// Este DTO é USADO APENAS PARA ENVIAR DADOS AO SERVIDOR (POST)
data class ActivityCreateDTO(
    @SerializedName("activity")
    val activity: String,
    // Note que o 'id' e 'date' estão ausentes, garantindo um JSON limpo
)