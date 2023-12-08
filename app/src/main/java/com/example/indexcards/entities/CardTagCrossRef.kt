package com.example.indexcards.entities

import androidx.room.Entity

@Entity(primaryKeys = ["cardId", "tagId"])
data class CardTagCrossRef(
    val cardId: Long,
    val tagId: Long,
)
