package com.example.indexcards.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Box(
    @PrimaryKey(autoGenerate = true)
    val boxId: Long,
    val name: String,
    val topic: String,
    val description: String,
    val dateAdded: Long,
)

fun Box.isLanguage(): Boolean {
    return (LanguageData.language.values.contains(this.topic))
}