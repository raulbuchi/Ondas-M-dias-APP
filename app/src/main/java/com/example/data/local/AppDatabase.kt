package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.NewsItemEntity
import com.example.data.model.PublicationEntity
import com.example.data.model.ResearchSurveyEntity

@Database(
    entities = [PublicationEntity::class, NewsItemEntity::class, ResearchSurveyEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun publicationDao(): PublicationDao
    abstract fun newsDao(): NewsDao
    abstract fun researchSurveyDao(): ResearchSurveyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ondas_medias_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
