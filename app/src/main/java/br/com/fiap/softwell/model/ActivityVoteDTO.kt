package br.com.fiap.softwell.model

import com.google.gson.annotations.SerializedName

data class ActivityVoteDTO(
    // ID da atividade escolhida pelo usuário (vem da Collection de Opções)
    @SerializedName("activityId")
    val activityId: String

    // O backend (Spring) adicionará a data/hora e o ID do usuário (se necessário)
    // na Collection de Votos.
)