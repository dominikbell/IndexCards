package com.example.indexcards.data

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getAllBoxesStream(): Flow<List<Box>>

    fun getBoxStream(id: Long): Flow<Box>

    suspend fun insertBox(box: Box)

    suspend fun deleteBox(box: Box)

    suspend fun updateBox(box: Box)
}