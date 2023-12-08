package com.example.indexcards.entities

import androidx.room.Entity

@Entity(primaryKeys = ["cardId", "tagId"])
data class CrossRef(
    val cardId: Long,
    val tagInt: Long,
)
