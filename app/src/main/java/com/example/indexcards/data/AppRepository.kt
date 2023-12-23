package com.example.indexcards.data

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getAllBoxesStream(): Flow<List<Box>>

    fun getBox(id: Long): Flow<Box>

    fun getNumberOfCards(boxId: Long): Int

    fun getBoxWithCardsStream(boxId: Long): Flow<BoxWithCards>

    suspend fun insertBox(box: Box)

    suspend fun deleteBox(box: Box)

    suspend fun updateBox(box: Box)
}