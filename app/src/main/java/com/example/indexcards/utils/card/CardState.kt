package com.example.indexcards.utils.card

import com.example.indexcards.data.Card
import com.example.indexcards.data.Tag
import java.time.LocalDateTime
import java.time.ZoneOffset

val emptyCard: Card = Card(
    cardId = -1,
    boxId = -1,
    word = "EMPTY CARD",
    meaning = "EMPTY CARD",
    notes = "EMPTY CARD",
    level = -1,
    dateAdded = 0,
)

data class CardState(
    val cardDetails: CardDetails = CardDetails(),
    val tagList: List<Tag> = listOf(),
    val isValid: Boolean = false
)

data class CardDetails(
    val id: Long = 0,
    val boxId: Long = 0,
    val word: String = "",
    val meaning: String = "",
    val notes: String = "",
    val level: Int = 0,
)

fun CardDetails.toCard(): Card = Card(
    cardId = id,
    boxId = boxId,
    word = word,
    meaning = meaning,
    notes = notes,
    level = level,
    dateAdded = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
)

fun Card.toCardDetails(): CardDetails = CardDetails(
    id = cardId,
    word = word,
    meaning = meaning,
    notes = notes,
    boxId = boxId,
    level = level
)

fun UiCardWithTags.toCardState(isValid: Boolean): CardState =
    CardState(
        cardDetails = card.toCardDetails(),
        tagList = tagList,
        isValid = isValid
    )

data class UiCardWithTags(
    val card: Card = emptyCard,
    val tagList: List<Tag> = listOf()
)