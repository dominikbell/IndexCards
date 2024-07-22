package com.example.indexcards.data

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Box(
    @PrimaryKey(autoGenerate = true)
    val boxId: Long,
    val name: String,
    val topic: String,
    @ColumnInfo(name = "reminders", defaultValue = "0")
    val reminders: Boolean,
    val description: String,
    val dateAdded: Long,
)

fun Box.isLanguage(): Boolean {
    return (LanguageData.language.map { it.value.first }.contains(this.topic))
}

/** Decided not to use emoji flags, but we will keep it in case it might come in handy */
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