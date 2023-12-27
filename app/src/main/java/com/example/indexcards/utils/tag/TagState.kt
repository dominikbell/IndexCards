package com.example.indexcards.utils.tag

import com.example.indexcards.data.Tag


data class TagState(
    val tagDetails: TagDetails = TagDetails(),
    val isValid: Boolean = false
)

data class TagDetails(
    val text: String = "",
    val color: String = "",
    val boxId: Long = 0,
)

fun TagDetails.toTag(): Tag = Tag(
    tagId = 0,
    text = text,
    color = color,
    boxId = boxId,
)