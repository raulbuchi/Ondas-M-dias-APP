package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "publications")
data class PublicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipientEmail: String,
    val recipientName: String,
    val title: String,
    val rawIdea: String,
    val fullMarkdown: String,
    val articleText: String,
    val audioScript: String,
    val imagePrompt: String,
    val timestamp: Long = System.currentTimeMillis()
)
