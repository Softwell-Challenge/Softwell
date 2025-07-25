package br.com.fiap.softwell.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "psycho_social")
data class PsychoSocial(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workloadAssessment: String,
    val qualityOfLifeImpact: String,
    val extraHours: String,
    val warningSigns: String,
    val mentalHealthImpact: String,
    val bossRating: Int,
    val coworkerRating: Int,
    val coworkerRespect: Int,
    val teamRelationship: Int,
    val freedomSpeech: Int,
    val welcomedPart: Int,
    val cooperationSpirit: Int,
    //verificar o que é o 3f
    val taskClarity: Float,
    val openCommunication: Float,
    val infoFlow: Float,
    val goalClarity: Float,
    val leaderCaresWellbeing: Float,
    val comfortableReportingIssues: Float,
    val leaderRecognizesEfforts: Float,
    val trustAndTransparency: Float
)
