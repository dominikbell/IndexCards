package com.example.indexcards.utils.box

import com.example.indexcards.data.Box
import java.time.LocalDateTime
import java.time.ZoneOffset

data class BoxState(
    val boxDetails: BoxDetails = BoxDetails(),
    val isValid: Boolean = false,
)

data class BoxDetails(
    val id: Long = 0,
    val name: String = "",
    val topic: String = "",
    val description: String = "",
)

fun BoxDetails.toBox(): Box = Box(
    boxId = id,
    name = name,
    topic = topic,
    description = description,
    dateAdded = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
)

fun Box.toBoxState(isValid: Boolean = false) : BoxState = BoxState(
    boxDetails = this.toBoxDetails(),
    isValid = isValid
)

fun Box.toBoxDetails(): BoxDetails = BoxDetails(
    id = boxId,
    name = name,
    topic = topic,
    description = description
)