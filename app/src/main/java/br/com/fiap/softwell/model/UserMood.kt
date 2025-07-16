package br.com.fiap.softwell.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_mood")
data class UserMood(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val moodId: String,
    val emoji: String,
    val timestamp: Long // salva a data/hora que foi preenchido
)
