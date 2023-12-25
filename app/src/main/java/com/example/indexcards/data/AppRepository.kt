package com.example.indexcards.data

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getAllBoxesStream(): Flow<List<Box>>

    fun getBoxStream(id: Long): Flow<Box>

    fun getNumberOfCards(boxId: Long): Flow<Int>

    fun getBoxWithCardsStream(boxId: Long): Flow<BoxWithCards>

    suspend fun insertBox(box: Box)

    suspend fun deleteBox(boxId: Long)

    suspend fun updateBox(box: Box)

    suspend fun insertCard(card: Card)

    suspend fun deleteCard(cardId: Long)

    suspend fun updateCard(card: Card)
}