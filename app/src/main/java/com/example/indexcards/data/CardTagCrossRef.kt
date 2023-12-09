package com.example.indexcards.data

import androidx.room.Entity

@Entity(primaryKeys = ["cardId", "tagId"])
data class CardTagCrossRef(
    val cardId: Long,
    val tagId: Long,
)
