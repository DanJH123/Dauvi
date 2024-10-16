package com.dapps.dauvi.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

 const val HOUR_IN_SECONDS = 3600

fun Long.secondsToDateString(
    locale: Locale = Locale.getDefault(),
    pattern: String = "HH:mm dd MMM",
): String {
    val date = Date(this * 1000)
    val format = SimpleDateFormat(pattern, locale)
    return format.format(date)
}

fun Long.roundDownToHour(): Long =
    if(this == 0L) 0
    else (this / HOUR_IN_SECONDS) * HOUR_IN_SECONDS

fun Long.roundUpToHour(): Long =
    if(this == 0L) 0
    else (this / HOUR_IN_SECONDS + 1) * HOUR_IN_SECONDS