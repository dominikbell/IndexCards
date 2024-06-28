package com.example.indexcards.data

import androidx.room.Embedded
import androidx.room.Relation

data class BoxWithCards(
    @Embedded val box: Box,
    @Relation(
        parentColumn = "boxId",
        entityColumn = "boxId",
    )
    val cards: List<Card>
)
