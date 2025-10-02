package br.com.fiap.softwell.model

import androidx.room.Entity

// DTO Principal para envio
@Entity(tableName = "psycho_social")
data class PsychoSocial(
    val userId: String,
    val workload: Workload,
    val warningSigns: WarningSigns,
    val relationshipClimate: RelationshipClimate,
    val communication: Communication,
    val leadershipRelation: LeadershipRelation
)

data class Workload(
    val workloadAssessment: String,
    val qualityOfLifeImpact: String,
    val extraHours: String
)

data class WarningSigns(
    val warningSigns: String,
    val mentalHealthImpact: String
)

data class RelationshipClimate(
    val bossRating: Int,
    val coworkerRating: Int,
    val coworkerRespect: Int,
    val teamRelationship: Int,
    val freedomSpeech: Int,
    val welcomedPart: Int,
    val cooperationSpirit: Int
)

data class Communication(
    val taskClarity: Float,
    val openCommunication: Float,
    val infoFlow: Float,
    val goalClarity: Float
)

data class LeadershipRelation(
    val leaderCaresWellbeing: Float,
    val leaderIsAvailable: Float,
    val comfortableReportingIssues: Float,
    val leaderRecognizesEfforts: Float,
    val trustAndTransparency: Float
)
