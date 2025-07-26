package br.com.fiap.softwell.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.Date

@Entity(tableName = "support")
data class Support(
    @PrimaryKey(autoGenerate = true)val id: Long = 0,
    val optionSupport: String,
    val Date: Long
)
