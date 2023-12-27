package com.example.indexcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Upsert
    suspend fun upsertBox(box: Box)

    @Query("DELETE FROM box WHERE boxId = :boxId")
    suspend fun deleteBox(boxId: Long)

    @Upsert
    suspend fun upsertCard(card: Card)

    @Query("DELETE FROM card WHERE cardId = :cardId")
    suspend fun deleteCard(cardId: Long)

    @Upsert
    suspend fun upsertTag(tag: Tag)

    @Query("DELETE FROM tag WHERE tagId = :tagId")
    suspend fun deleteTag(tagId: Long)

    @Query("SELECT * FROM box ORDER BY dateAdded DESC")
    fun getAllBoxes(): Flow<List<Box>>

    @Query("SELECT * FROM box WHERE boxId = :id")
    fun getBox(id: Long): Flow<Box>

    @Query("SELECT * FROM box WHERE boxId = :boxId")
    fun getBoxWithCards(boxId: Long): Flow<BoxWithCards>

    @Query("SELECT * FROM box WHERE boxId = :boxId")
    fun getTagsOfBox(boxId: Long): Flow<BoxWithTags>

    @Query("SELECT COUNT(*) from card WHERE boxId = :boxId")
    fun getNumberOfCards(boxId: Long): Flow<Int>

    @Transaction
    @Query("SELECT * FROM tag WHERE tagId = :tagId")
    fun getCardsOfTag(tagId: Long): Flow<TagWithCards>

    @Transaction
    @Query("SELECT * FROM card WHERE cardId = :cardId")
    fun getTagsOfCard(cardId: Long): Flow<CardWithTags>
}