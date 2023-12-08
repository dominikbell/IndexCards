package com.example.indexcards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.indexcards.entities.Card
import com.example.indexcards.entities.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Upsert
    suspend fun upsertCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)

    @Upsert
    suspend fun upsertTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT * FROM cards WHERE language = :lang")
    suspend fun getCards(lang: String): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE language = :lang & level = :lev")
    suspend fun getCardsInLevel(lang: String, lev: Int): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE language = :lang ORDER BY dateAdded DESC")
    suspend fun getCardsSortedByDate(lang: String): Flow<List<Card>>
}