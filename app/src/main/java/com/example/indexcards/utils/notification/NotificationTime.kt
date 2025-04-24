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
    return ZonedDateTime.now().plusMonths(months.toLong()).plusWeeks(weeks.toLong())
        .plusDays(days.toLong()).withHour(setHour).withMinute(setMinute).toInstant().toEpochMilli()
}

/** Get time in epoch milliseconds in a given amount of days/weeks/months/hours/minutes */
fun getTimeInTheFuture(
    months: Int = 0,
    weeks: Int = 0,
    days: Int = 0,
    hours: Int = 0,
    minutes: Int = 0,
): Long {
    return ZonedDateTime.now().plusMonths(months.toLong()).plusWeeks(weeks.toLong())
        .plusDays(days.toLong()).plusHours(hours.toLong()).plusMinutes(minutes.toLong()).toInstant()
        .toEpochMilli()
}

fun getMonthsWeeksDays(
    reminderIntervals: List<Pair<Int, String>>,
    level: Int,
): Triple<Int, Int, Int> {
    val months = if (reminderIntervals[level].second == "m") {
        reminderIntervals[level].first
    } else {
        0
    }

    val weeks = if (reminderIntervals[level].second == "w") {
        reminderIntervals[level].first
    } else {
        0
    }

    val days = if (reminderIntervals[level].second == "d") {
        reminderIntervals[level].first
    } else {
        0
    }

    return Triple(months, weeks, days)
}

/** Get time interval for notifications of a level from reminderInterval */
fun getTimeInterval(
    reminderIntervals: List<Pair<Int, String>>,
    level: Int,
): Long {
    val times = getMonthsWeeksDays(reminderIntervals = reminderIntervals, level = level)

    val months = times.first
    val weeks = times.second
    val days = times.third

    val totalDays = (30 * months) + (7 * weeks) + days

    return (totalDays.toLong() * 24 * 60 * 60 * 1000)
}

/** Get time of notification of a level from reminderIntervals at reminderTime
 * - if no reminderTime is set: use current hour
 * - if reminderTime is later than current hour: subtract 1 from the number of days
 *   (so e.g. a reminder in 1d is already notified on that day)
 **/
fun getTriggerTime(
    reminderIntervals: List<Pair<Int, String>>,
    reminderTime: Pair<Int, Int>,
    level: Int,
): Long {
    val times = getMonthsWeeksDays(reminderIntervals = reminderIntervals, level = level)

    val months = times.first
    val weeks = times.second
    val days =
        /* If no reminder time is set: just remind at the current hour */
        if (reminderTime.first == -1) {
            times.third
        } else {
            /* If the reminder time is later than the current time: count today as one day
            * so e.g. 1day reminder interval will be reminded today */
            if (ZonedDateTime.now().hour <= reminderTime.first) {
                /* If it's the same hour: check the minute as well */
                if (ZonedDateTime.now().hour == reminderTime.first) {
                    /* Give 5 minutes extra since alarms are not exact */
                    if (ZonedDateTime.now().minute < reminderTime.first - 5) {
                        times.third - 1
                    } else {
                        times.third
                    }
                } else {
                    /* Current hour is definitely before reminder hour: count today as one day */
                    times.third - 1
                }
            } else {
                /* If the current time is later than the reminder time: remind normally the next scheduled day */
                times.third
            }
        }

    /* If no reminder time has been set: just remind at the time when this function is called*/
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