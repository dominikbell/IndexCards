package com.example.indexcards.utils.notification

import java.time.ZonedDateTime

/** Get time in epoch milliseconds at a given time in a given amount of days/weeks/months */
fun getTimeOfHourInTheFuture(
    months: Int = 0,
    weeks: Int = 0,
    days: Int = 0,
    setHour: Int = 0,
    setMinute: Int = 0,
): Long {
    return ZonedDateTime.now()
        .plusMonths(months.toLong())
        .plusWeeks(weeks.toLong())
        .plusDays(days.toLong())
        .withHour(setHour)
        .withMinute(setMinute)
        .toInstant()
        .toEpochMilli()
}

/** Get time in epoch milliseconds in a given amount of days/weeks/months/hours/minutes */
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

/** Get time in epoch milliseconds of a level from reminderIntervals at reminderTime */
fun getTimeFromReminderSettings(
    reminderIntervals: List<Pair<Int, String>>,
    reminderTime: Pair<Int, Int>,
    level: Int
): Long {
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

    return if (reminderTime.first == -1) {
        getTimeInTheFuture(months = months, weeks = weeks, days = days)
    } else {
        getTimeOfHourInTheFuture(
            months = months,
            weeks = weeks,
            days = days,
            setHour = reminderTime.first,
            setMinute = reminderTime.second
        )
    }
}