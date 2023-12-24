package com.example.indexcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Upsert
    suspend fun upsertBox(box: Box)

    @Delete
    suspend fun deleteBox(box: Box)

    @Upsert
    suspend fun upsertCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)

    @Upsert
    suspend fun upsertTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT * FROM box ORDER BY dateAdded DESC")
    fun getAllBoxes(): Flow<List<Box>>

    @Query("SELECT * FROM box WHERE boxId = :id")
    fun getBox(id: Long): Flow<Box>

    @Query("SELECT * FROM box WHERE boxId = :boxId")
    fun getBoxWithCards(boxId: Long): Flow<BoxWithCards>

    @Query("SELECT COUNT(*) from card WHERE boxId = :boxId")
    fun getNumberOfCards(boxId: Long): Flow<Int>
}