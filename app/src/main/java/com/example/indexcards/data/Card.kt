package com.example.indexcards.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Card")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val cardId: Long,
    val word: String,
    val meaning: String,
    val notes: String,
    val dateAdded: Long,
    val level: Int,
    val boxId: Long,
    @ColumnInfo(name = "memoURI", defaultValue = "")
    val memoURI: String,
    @ColumnInfo(name = "categoryId", defaultValue = "-1")
    val categoryId: Long
)
