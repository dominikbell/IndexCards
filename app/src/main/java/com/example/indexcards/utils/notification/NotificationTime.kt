package com.example.indexcards.utils.notification

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
