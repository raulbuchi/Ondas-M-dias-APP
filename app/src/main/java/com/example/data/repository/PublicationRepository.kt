package com.example.data.repository

import com.example.data.local.PublicationDao
import com.example.data.model.PublicationEntity
import com.example.data.remote.GeminiService
import com.example.data.remote.ParsedOutput
import kotlinx.coroutines.flow.Flow

class PublicationRepository(
    private val publicationDao: PublicationDao,
    private val geminiService: GeminiService
) {
    val allPublications: Flow<List<PublicationEntity>> = publicationDao.getAllPublications()

    suspend fun generateContent(
        recipientEmail: String,
        title: String,
        idea: String,
        customApiKey: String? = null
    ): Result<ParsedOutput> {
        return geminiService.generateMultimodalContent(
            recipientEmail = recipientEmail,
            title = title,
            idea = idea,
            customApiKey = customApiKey
        )
    }

    suspend fun savePublication(
        recipientEmail: String,
        recipientName: String,
        title: String,
        rawIdea: String,
        parsedOutput: ParsedOutput
    ): Long {
        val entity = PublicationEntity(
            recipientEmail = recipientEmail,
            recipientName = recipientName,
            title = title,
            rawIdea = rawIdea,
            fullMarkdown = parsedOutput.fullMarkdown,
            articleText = parsedOutput.articleText,
            audioScript = parsedOutput.audioScript,
            imagePrompt = parsedOutput.imagePrompt
        )
        return publicationDao.insertPublication(entity)
    }

    suspend fun deletePublication(id: Long) {
        publicationDao.deleteById(id)
    }

    suspend fun clearHistory() {
        publicationDao.clearAll()
    }
}
