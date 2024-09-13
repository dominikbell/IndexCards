package com.example.indexcards.utils.state

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
    dateAdded = -1,
    memoURI = "",
    categoryId = -1,
)

data class CardState(
    val cardDetails: CardDetails = CardDetails(),
    val tagList: List<Tag> = listOf(),
    val isValid: Boolean = false,
    val validWord: Boolean = false,
    val validMeaning: Boolean = false,
)

data class CardDetails(
    val id: Long = -1,
    val boxId: Long = -1,
    val word: String = "",
    val meaning: String = "",
    val notes: String = "",
    val level: Int = 0,
    val memoURI: String = "",
    val dateAdded: Long = -1,
    val categoryId: Long = -1,
)

fun CardDetails.toCard(): Card = Card(
    cardId = id,
    boxId = boxId,
    word = word,
    meaning = meaning,
    notes = notes,
    level = level,
    dateAdded = if (dateAdded == (-1).toLong()) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) else dateAdded,
    memoURI = memoURI,
    categoryId = categoryId,
)

fun Card.toCardDetails(): CardDetails = CardDetails(
    id = cardId,
    word = word,
    meaning = meaning,
    notes = notes,
    boxId = boxId,
    level = level,
    dateAdded = dateAdded,
    memoURI = memoURI,
    categoryId = categoryId,
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