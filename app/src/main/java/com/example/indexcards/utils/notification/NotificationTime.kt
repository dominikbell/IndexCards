package com.example.indexcards.utils.notification

import androidx.lifecycle.viewModelScope
import com.example.indexcards.NUMBER_OF_LEVELS
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

fun getTimeInTheFuture(
    months: Int = 0,
    weeks: Int = 0,
    days: Int = 0,
    hours: Int = 0,
    minutes: Int = 0,
): Long {
    return ZonedDateTime.now()
        .plusMonths(months.toLong())
        .plusWeeks(weeks.toLong())
        .plusDays(days.toLong())
        .plusHours(hours.toLong())
        .plusMinutes(minutes.toLong())
        .toInstant()
        .toEpochMilli()
}

fun getTimeFromReminderIntervals(reminderIntervals: List<Pair<Int, String>>, level: Int): Long {
        val months =
            if (reminderIntervals[level].second == "m") {
                reminderIntervals[level].first
            } else {
                0
            }
        val weeks =
            if (reminderIntervals[level].second == "w") {
                reminderIntervals[level].first
            } else {
                0
            }
        val days =
            if (reminderIntervals[level].second == "d") {
                reminderIntervals[level].first
            } else {
                0
            }
        return getTimeInTheFuture(months = months, weeks = weeks, days = days)
}