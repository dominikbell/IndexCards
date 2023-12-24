package com.example.indexcards.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tag")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long,
    val text: String,
)
