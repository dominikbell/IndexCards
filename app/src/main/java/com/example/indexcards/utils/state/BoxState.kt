package com.example.indexcards.utils.state

import android.content.Context
import com.example.indexcards.data.Box
import com.example.indexcards.data.Card
import com.example.indexcards.data.LanguageData
import java.time.LocalDateTime
import java.time.ZoneOffset

val emptyBox: Box = Box(
    boxId = -1,
    name = "EMPTY BOX",
    topic = "EMPTY BOX",
    description = "EMPTY BOX",
    reminders = false,
    categories = false,
    dateAdded = -1,
)

data class BoxState(
    val boxDetails: BoxDetails = BoxDetails(),
    val isValid: Boolean = false,
    val validName: Boolean = false,
    val validTopic: Boolean = false,
)

data class BoxDetails(
    val id: Long = -1,
    val name: String = "",
    val topic: String = "",
    val reminders: Boolean = false,
    val categories: Boolean = false,
    val description: String = "",
    val dateAdded: Long = -1,
)

fun BoxDetails.toBox(): Box = Box(
    boxId = id,
    name = name,
    topic = topic,
    description = description,
    reminders = reminders,
    categories = categories,
    dateAdded = if (dateAdded == (-1).toLong()) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) else dateAdded,
)

fun Box.toBoxState(isValid: Boolean = false) : BoxState = BoxState(
    boxDetails = this.toBoxDetails(),
    isValid = isValid
)

fun Box.toBoxDetails(): BoxDetails = BoxDetails(
    id = boxId,
    name = name,
    topic = topic,
    reminders = reminders,
    categories = categories,
    description = description,
    dateAdded = dateAdded,
)

data class UiBoxWithCards(
    val box: Box = emptyBox,
    val cardList: List<Card> = listOf()
)

fun Box.isLanguage(): Boolean {
    return (LanguageData.language.map { it.value.first }.contains(this.topic))
}

/** Decided not to use emoji flags, but will keep it in case it might come in handy */
fun Box.namePlusFlag(): String {
    return this.name +
            if (this.isLanguage()) {
                " " + this.getFlag()
            } else {
                ""
            }
}

fun Box.getFlag(): String {
    return if (this.isLanguage()) {
        LanguageData.language.values.find { it.first == this.topic }?.second ?: ""
    } else {
        ""
    }
}

fun Box.getImageId(context: Context): Int {
    return if (this.isLanguage()) {
        getImageId(
            context = context,
            nameBase = LanguageData.language.filterValues { it.first == this.topic }.keys.first()
        )
    } else {
        -1
    }
}

fun getImageId(context: Context, nameBase: String): Int {
    return context.resources.getIdentifier(
        "flag$nameBase",
        "drawable",
        context.packageName
    )
}
