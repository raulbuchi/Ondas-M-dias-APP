package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ResearchSurveyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResearchSurveyDao {
    @Query("SELECT * FROM research_surveys ORDER BY timestamp DESC")
    fun getAllSurveys(): Flow<List<ResearchSurveyEntity>>

    @Query("SELECT * FROM research_surveys WHERE id = :id")
    suspend fun getSurveyById(id: Long): ResearchSurveyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurveys(surveys: List<ResearchSurveyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurvey(survey: ResearchSurveyEntity): Long

    @Update
    suspend fun updateSurvey(survey: ResearchSurveyEntity)

    @Query("DELETE FROM research_surveys WHERE id = :id")
    suspend fun deleteSurvey(id: Long)
}
