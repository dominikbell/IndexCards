package com.example.indexcards.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.coroutines.flow.Flow

data class BoxWithCards(
    @Embedded val box: Box,
    @Relation(
        parentColumn = "boxId",
        entityColumn = "boxId",
    )
    val cards: List<Card>
)
