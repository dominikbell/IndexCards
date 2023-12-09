package com.example.indexcards.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val cardId: Long,
    val word: String,
    val meaning: String,
    val dateAdded: Int,
    val level: Int,
    val boxId: Long,
)
