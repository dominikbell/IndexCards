package com.example.indexcards.utils.tag

import androidx.compose.ui.graphics.Color
import com.example.indexcards.data.Tag

val emptyTag: Tag = Tag(
    boxId = -1,
    color = "#000000",
    tagId = -1,
    text = "EMPTY TAG"
)

data class TagState(
    val tagDetails: TagDetails = TagDetails(),
    val isValid: Boolean = false
)

data class TagDetails(
    val id: Long = 0,
    val text: String = "",
    val color: String = "",
    val boxId: Long = 0,
)

fun TagDetails.toTag(): Tag = Tag(
    tagId = id,
    text = text,
    color = color,
    boxId = boxId,
)

fun Tag.toTagState(isValid: Boolean = false): TagState = TagState(
    tagDetails = this.toTagDetails(),
    isValid = isValid
)

fun Tag.toTagDetails(): TagDetails = TagDetails(
    id = tagId,
    text = text,
    color = color,
    boxId = boxId,
)

data class UiColorState(
    val color: String = "#FFFFFF"
)

fun UiColorState.toColor(): Color {
    return Color(android.graphics.Color.parseColor(color))
}