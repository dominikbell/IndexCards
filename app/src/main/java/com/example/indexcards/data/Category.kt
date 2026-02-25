package com.example.indexcards.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long,
    val boxId: Long,
    val name: String,
)
