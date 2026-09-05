package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CreationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreationDao {

    @Query("SELECT * FROM creations ORDER BY timestamp DESC")
    fun getAllCreations(): Flow<List<CreationEntity>>

    @Query("SELECT * FROM creations WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteCreations(): Flow<List<CreationEntity>>

    @Query("SELECT * FROM creations WHERE type IN ('TEXT_TO_VIDEO', 'IMAGE_TO_VIDEO') ORDER BY timestamp DESC")
    fun getVideoCreations(): Flow<List<CreationEntity>>

    @Query("SELECT * FROM creations WHERE type IN ('TEXT_TO_IMAGE', 'IMAGE_TO_IMAGE') ORDER BY timestamp DESC")
    fun getImageCreations(): Flow<List<CreationEntity>>

    @Query("SELECT * FROM creations WHERE id = :id LIMIT 1")
    suspend fun getCreationById(id: Long): CreationEntity?

    @Query("SELECT * FROM creations ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentCreations(limit: Int = 10): Flow<List<CreationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreation(creation: CreationEntity): Long

    @Update
    suspend fun updateCreation(creation: CreationEntity)

    @Delete
    suspend fun deleteCreation(creation: CreationEntity)

    @Query("DELETE FROM creations WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE creations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM creations")
    suspend fun clearAll()
}
