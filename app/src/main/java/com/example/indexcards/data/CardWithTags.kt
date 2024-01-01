package com.example.indexcards.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CardWithTags(
    @Embedded val card: Card,
    @Relation(
        parentColumn = "cardId",
        entityColumn = "tagId",
        associateBy = Junction(TagCardCrossRef::class)
    )
    val tags: List<Tag>
)
