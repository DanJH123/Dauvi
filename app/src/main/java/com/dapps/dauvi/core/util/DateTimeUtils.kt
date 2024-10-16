package com.dapps.dauvi.core.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun currentTimeSeconds(): Long = System.currentTimeMillis() / 1000

fun generateEpochTimeInSeconds(hour: Int, minute: Int = 0, second: Int = 0, millisecond: Int = 0): Long {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, second)
    calendar.set(Calendar.MILLISECOND, millisecond)

    return calendar.timeInMillis / 1000
}

fun timeStringToLongSeconds(timeString: String, timePattern: String, timezoneId: String): Long {
    val formatter = SimpleDateFormat(timePattern, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone(timezoneId)
    val date = formatter.parse(timeString)

    val calendar = Calendar.getInstance(TimeZone.getTimeZone(timezoneId)).apply {
        time = date ?: throw IllegalArgumentException("Invalid time string")
    }

    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone(timezoneId)).apply {
        set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return utcCalendar.timeInMillis / 1000

}