package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class SurveyOption(
    val name: String,
    val percentage: Double,
    val colorHex: String? = null
)

@Entity(tableName = "research_surveys")
data class ResearchSurveyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val institute: String,
    val registrationId: String,
    val category: String, // POLÍTICA, INFRAESTRUTURA, AVALIAÇÃO GOVERNAMENTAL
    val region: String, // Matinhos, Litoral PR, Paraná
    val sampleSize: Int,
    val marginOfError: Double,
    val datePeriod: String,
    val optionsJson: String, // JSON array of SurveyOption
    val analyticalReport: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
