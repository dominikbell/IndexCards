package com.example.indexcards.entities

import androidx.room.Embedded
import androidx.room.Relation

data class BoxWithCards(
    @Embedded val box: Box,
    @Relation(
        parentColumn = "boxId",
        entityColumn = "cardId",
    )
    val cards: List<Card>
)
