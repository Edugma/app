package io.edugma.core.api.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJSDate

private fun Instant.format(format: String): String {
    return this.toJSDate()
        .toISOString()
}

actual fun LocalDateTime.format(format: String): String {
    return this.toInstant(TimeZone.currentSystemDefault())
        .toJSDate()
        .toISOString()
}

actual fun LocalDate.format(
    format: DateFormat,
): String {
    return this.atTime(LocalTime(1, 1, 1, 1))
        .toInstant(TimeZone.currentSystemDefault())
        .toJSDate()
        .toDateString()
}

actual fun LocalTime.format(
    format: TimeFormat,
): String {
    return this.atDate(LocalDate(2018, 1, 1))
        .toInstant(TimeZone.currentSystemDefault())
        .toJSDate()
        .toTimeString()
}

actual fun DayOfWeek.format(
    format: String,
): String {
    return this.name.lowercase().capitalized()
}

actual fun Month.format(
    format: String,
): String {
    return this.name.lowercase().capitalized()
}
