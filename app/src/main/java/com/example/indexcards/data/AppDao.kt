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

    @Upsert
    suspend fun upsertCard(card: Card)

    @Upsert
    suspend fun upsertTag(tag: Tag)

    @Query("UPDATE tag SET text = :text, color = :color WHERE tagId = :tagId")
    suspend fun updateTag(tagId: Long, text: String, color: String)

    @Upsert
    suspend fun upsertTagCardCrossRef(crossRef: TagCardCrossRef)

    @Query("DELETE FROM box WHERE boxId = :boxId")
    suspend fun deleteBox(boxId: Long)

    @Query("DELETE FROM card WHERE cardId = :cardId")
    suspend fun deleteCard(cardId: Long)

    @Query("DELETE FROM tag WHERE tagId = :tagId")
    suspend fun deleteTag(tagId: Long)

    @Delete
    suspend fun deleteTagCardCrossRef(crossRef: TagCardCrossRef)

    @Query("DELETE FROM card WHERE boxId = :boxId")
    suspend fun deleteCardsFromBox(boxId: Long)

    @Query("DELETE FROM tag WHERE boxId = :boxId")
    suspend fun deleteTagsFromBox(boxId: Long)

    @Query("DELETE FROM tagcardcrossref WHERE tagId = :tagId")
    suspend fun deleteTagsFromCard(tagId: Long)

    @Query("DELETE FROM tagcardcrossref WHERE cardId = :cardId")
    suspend fun deleteCardFromTags(cardId: Long)

    @Query("SELECT * FROM box ORDER BY dateAdded DESC")
    fun getAllBoxes(): Flow<List<Box>>

    @Query("SELECT * FROM box WHERE boxId = :boxId")
    fun getBox(boxId: Long): Flow<Box>

    @Query("SELECT * FROM card WHERE cardId = :cardId")
    fun getCard(cardId: Long): Flow<Card>

    @Query("SELECT * FROM tag WHERE tagId = :tagId")
    fun getTag(tagId: Long): Flow<Tag>

    @Query("SELECT * FROM box WHERE boxId = :boxId")
    fun getBoxWithCards(boxId: Long): Flow<BoxWithCards>

    @Query("SELECT * FROM box WHERE boxId = :boxId")
    fun getBoxWithTags(boxId: Long): Flow<BoxWithTags>

    @Transaction
    @Query("SELECT * FROM tag WHERE tagId = :tagId")
    fun getTagWithCards(tagId: Long): Flow<TagWithCards>

    @Transaction
    @Query("SELECT * FROM card WHERE cardId = :cardId")
    fun getCardWithTags(cardId: Long): Flow<CardWithTags>

    @Transaction
    @Query("SELECT * FROM card WHERE boxId = :boxId")
    fun getAllCardsWithTagsOfBox(boxId: Long): Flow<List<CardWithTags>>

    @Query("SELECT MAX(cardId) FROM card")
    suspend fun getBiggestCardId(): Long?

    @Query("UPDATE Card SET level = level + 1 WHERE cardId = :cardId")
    suspend fun upgradeLevelOnCard(cardId: Long)

    @Query("UPDATE Card SET level = level - 1 WHERE cardId = :cardId")
    suspend fun downgradeLevelOnCard(cardId: Long)
}