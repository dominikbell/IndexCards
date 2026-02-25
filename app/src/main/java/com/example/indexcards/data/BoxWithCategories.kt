package com.example.indexcards.data

import androidx.room.Embedded
import androidx.room.Relation

data class BoxWithCategories(
    @Embedded val box: Box,
    @Relation(
        parentColumn = "boxId",
        entityColumn = "boxId",
    )
    val categories: List<Category>
)
