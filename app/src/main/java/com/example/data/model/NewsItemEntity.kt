package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_items")
data class NewsItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // POLÍTICA, GESTÃO GOVERNAMENTAL, INFRAESTRUTURA, SOCIEDADE
    val location: String, // Matinhos, Litoral PR, Curitiba/PR, Paraná
    val summary: String,
    val fullContent: String,
    val source: String, // Portal Ondas Médias, Agência PR, etc.
    val isBookmarked: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
