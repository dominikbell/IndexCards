package com.example.indexcards.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.coroutines.flow.Flow

data class CardWithTags(
    @Embedded val card: Card,
    @Relation(
        parentColumn = "cardId",
        entityColumn = "tagId",
        associateBy = Junction(CardTagCrossRef::class)
    )
    val tags: Flow<List<Tag>>
)
