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
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter

private fun Instant.format(format: String): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = format
    return dateFormatter.stringFromDate(
        this.toNSDate(),
    )
}

actual fun LocalDateTime.format(format: String): String {
    return this.toInstant(TimeZone.currentSystemDefault())
        .format(format)
}

actual fun LocalDate.format(
    format: String,
): String {
    return this.atTime(LocalTime(1, 1, 1, 1))
        .toInstant(TimeZone.currentSystemDefault())
        .format(format)
}

actual fun LocalTime.format(
    format: String,
): String {
    return this.atDate(LocalDate(2018, 1, 1))
        .toInstant(TimeZone.currentSystemDefault())
        .format(format)
}

actual fun DayOfWeek.format(
    format: String,
): String {
    return LocalDateTime(2018, 1, this.isoDayNumber, 1, 1, 1, 1)
        .toInstant(TimeZone.currentSystemDefault())
        .format(format)
}

actual fun Month.format(
    format: String,
): String {
    return LocalDateTime(2018, (this.ordinal + 1), 1, 1, 1, 1, 1)
        .toInstant(TimeZone.currentSystemDefault())
        .format(format)
}
