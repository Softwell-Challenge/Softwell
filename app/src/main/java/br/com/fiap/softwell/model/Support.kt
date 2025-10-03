package br.com.fiap.softwell.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "support")
data class Support(
    @PrimaryKey(autoGenerate = true)val id: Long = 0,
    val optionSupport: String,
    val Date: Long
)
