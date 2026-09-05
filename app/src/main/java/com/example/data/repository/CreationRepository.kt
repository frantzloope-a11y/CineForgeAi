package com.example.data.repository

import com.example.data.local.CreationDao
import com.example.data.model.CreationEntity
import kotlinx.coroutines.flow.Flow

class CreationRepository(private val dao: CreationDao) {

    val allCreations: Flow<List<CreationEntity>> = dao.getAllCreations()
    val favoriteCreations: Flow<List<CreationEntity>> = dao.getFavoriteCreations()
    val videoCreations: Flow<List<CreationEntity>> = dao.getVideoCreations()
    val imageCreations: Flow<List<CreationEntity>> = dao.getImageCreations()

    fun getRecentCreations(limit: Int = 10): Flow<List<CreationEntity>> = dao.getRecentCreations(limit)

    suspend fun getById(id: Long): CreationEntity? = dao.getCreationById(id)

    suspend fun saveCreation(creation: CreationEntity): Long = dao.insertCreation(creation)

    suspend fun deleteById(id: Long) = dao.deleteById(id)

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = dao.updateFavorite(id, isFavorite)

    suspend fun clearHistory() = dao.clearAll()
}
