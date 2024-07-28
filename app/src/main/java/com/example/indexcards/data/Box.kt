package com.example.indexcards.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Box(
    @PrimaryKey(autoGenerate = true)
    val boxId: Long,
    val name: String,
    val topic: String,
    @ColumnInfo(name = "reminders", defaultValue = "0")
    val reminders: Boolean,
    val description: String,
    val dateAdded: Long,
)