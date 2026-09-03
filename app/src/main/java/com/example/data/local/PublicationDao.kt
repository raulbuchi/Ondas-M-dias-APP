package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.PublicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PublicationDao {
    @Query("SELECT * FROM publications ORDER BY timestamp DESC")
    fun getAllPublications(): Flow<List<PublicationEntity>>

    @Query("SELECT * FROM publications WHERE id = :id LIMIT 1")
    suspend fun getPublicationById(id: Long): PublicationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPublication(publication: PublicationEntity): Long

    @Delete
    suspend fun deletePublication(publication: PublicationEntity)

    @Query("DELETE FROM publications WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM publications")
    suspend fun clearAll()
}
