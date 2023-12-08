package com.example.indexcards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.indexcards.entities.Box
import com.example.indexcards.entities.BoxWithCards
import com.example.indexcards.entities.Card
import com.example.indexcards.entities.Tag

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

    @Query("SELECT * FROM box WHERE boxId = :boxId")
    suspend fun getBoxWithCards(boxId: Long): List<BoxWithCards>
}