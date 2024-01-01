package com.example.indexcards.data

import androidx.room.Entity

@Entity(primaryKeys = ["tagId", "cardId"])
data class TagCardCrossRef(
    val tagId: Long,
    val cardId: Long,
)
