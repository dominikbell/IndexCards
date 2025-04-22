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
    @ColumnInfo(name = "categories", defaultValue = "0")
    val categories: Boolean,
    val description: String,
    val dateAdded: Long,
    @ColumnInfo(name = "showNumberOfCards", defaultValue = "1")
    val showNumberOfCards: Boolean,
    @ColumnInfo(name = "lastTrained1", defaultValue = "-1")
    val lastTrained1: Long,
    @ColumnInfo(name = "lastTrained2", defaultValue = "-1")
    val lastTrained2: Long,
    @ColumnInfo(name = "lastTrained3", defaultValue = "-1")
    val lastTrained3: Long,
    @ColumnInfo(name = "lastTrained4", defaultValue = "-1")
    val lastTrained4: Long,
    @ColumnInfo(name = "lastTrained5", defaultValue = "-1")
    val lastTrained5: Long,
)