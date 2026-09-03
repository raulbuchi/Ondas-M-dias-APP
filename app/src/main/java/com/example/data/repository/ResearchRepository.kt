package com.example.data.repository

import com.example.data.local.ResearchSurveyDao
import com.example.data.model.ResearchSurveyEntity
import com.example.data.remote.ResearchSurveyProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ResearchRepository(private val surveyDao: ResearchSurveyDao) {

    val allSurveys: Flow<List<ResearchSurveyEntity>> = surveyDao.getAllSurveys()

    suspend fun seedInitialDataIfNeeded() {
        val current = surveyDao.getAllSurveys().first()
        if (current.isEmpty()) {
            surveyDao.insertSurveys(ResearchSurveyProvider.getDefaultSurveys())
        }
    }

    suspend fun saveSurvey(survey: ResearchSurveyEntity): Long {
        return surveyDao.insertSurvey(survey)
    }

    suspend fun updateSurvey(survey: ResearchSurveyEntity) {
        surveyDao.updateSurvey(survey)
    }

    suspend fun deleteSurvey(id: Long) {
        surveyDao.deleteSurvey(id)
    }
}
