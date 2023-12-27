package com.example.indexcards.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.coroutines.flow.Flow

data class TagWithCards(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "cardId",
        associateBy = Junction(CardTagCrossRef::class)
    )
    val cards: List<Card>
)
