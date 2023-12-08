package com.example.indexcards.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Box(
    @PrimaryKey(autoGenerate = true)
    val boxId: Long,
    val name: String,
    val languageOrTopic: String,
)
