package com.example.indexcards.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BoxWithCards(
    @Embedded val box: Box,
    @Relation(
        parentColumn = "boxId",
        entityColumn = "cardId",
    )
    val cards: List<Card>
)
