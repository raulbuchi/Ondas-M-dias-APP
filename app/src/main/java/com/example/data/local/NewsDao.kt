package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.NewsItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_items ORDER BY timestamp DESC")
    fun getAllNews(): Flow<List<NewsItemEntity>>

    @Query("SELECT * FROM news_items WHERE isBookmarked = 1 ORDER BY timestamp DESC")
    fun getBookmarkedNews(): Flow<List<NewsItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(newsList: List<NewsItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleNews(news: NewsItemEntity): Long

    @Update
    suspend fun updateNews(news: NewsItemEntity)

    @Query("DELETE FROM news_items WHERE isBookmarked = 0")
    suspend fun clearUnbookmarkedNews()

    @Query("DELETE FROM news_items WHERE id = :id")
    suspend fun deleteNewsById(id: Long)
}
