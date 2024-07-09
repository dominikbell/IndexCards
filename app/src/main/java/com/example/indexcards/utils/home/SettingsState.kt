package com.example.indexcards.utils.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.indexcards.CHOICES_FOR_REMINDER_INTERVALS
import com.example.indexcards.NUMBER_OF_LEVELS
import com.example.indexcards.R
import com.example.indexcards.utils.DefaultPreferences

data class UiSettings(
    val settingsDetails: SettingsDetails = SettingsDetails(),
    val isValid: Boolean = false,
)

data class SettingsDetails(
    val userName: String = "",
    val globalReminders: Boolean = false,
    val reminderIntervals: List<Pair<Int, String>> = DefaultPreferences.REMINDER_INTERVALS
)

fun SettingsDetails.isValid(): Boolean {
    return (
            this.userName.isNotBlank() &&
                    this.reminderIntervals.size == NUMBER_OF_LEVELS &&
                    this.reminderIntervals.all { it.first > 0 } &&
                    this.reminderIntervals.all { CHOICES_FOR_REMINDER_INTERVALS.contains(it.second) }
            )
}

@Composable
fun String.toReminderText(interval: Int): String {
    assert(this.length == 1)

    /* TODO: is not quite correct since german words also have genders ... */

    if (interval == 1 || interval == -1) {
        val period =
            when (this) {
                "d" -> stringResource(id = R.string.day)
                "w" -> stringResource(id = R.string.week)
                "m" -> stringResource(id = R.string.month)
                else -> stringResource(id = R.string.error)
            }

        return stringResource(id = R.string.every_singular) + " " + period

    } else {
        val period =
            when (this) {
                "d" -> stringResource(id = R.string.days)
                "w" -> stringResource(id = R.string.weeks)
                "m" -> stringResource(id = R.string.months)
                else -> stringResource(id = R.string.error)
            }

        return stringResource(id = R.string.every_plural) + " " + interval + " " + period
    }
}

@Composable
fun String.toWord(): String {
    return when (this) {
        "d" -> stringResource(id = R.string.day)
        "w" -> stringResource(id = R.string.week)
        "m" -> stringResource(id = R.string.month)
        else -> stringResource(id = R.string.error)
    }
}

@Composable
fun String.toWord(interval: Int): String {
    assert(this.length == 1)

    if (interval == 1) {
        return when (this) {
            "d" -> stringResource(id = R.string.day)
            "w" -> stringResource(id = R.string.week)
            "m" -> stringResource(id = R.string.month)
            else -> stringResource(id = R.string.error)
        }


    } else {
        return when (this) {
            "d" -> stringResource(id = R.string.days)
            "w" -> stringResource(id = R.string.weeks)
            "m" -> stringResource(id = R.string.months)
            else -> stringResource(id = R.string.error)
        }

    }
}

@Composable
fun Int.toWord(): String {
    return when (this) {
        1 -> stringResource(id = R.string.first)
        2 -> stringResource(id = R.string.second)
        3 -> stringResource(id = R.string.third)
        4 -> stringResource(id = R.string.fourth)
        5 -> stringResource(id = R.string.fifth)
        else -> stringResource(id = R.string.error)
    }
}