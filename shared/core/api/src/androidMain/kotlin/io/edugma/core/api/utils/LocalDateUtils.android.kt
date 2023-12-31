package io.edugma.core.api.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter

actual fun LocalDateTime.format(
    format: String,
): String = DateTimeFormatter.ofPattern(format).format(this.toJavaLocalDateTime())

actual fun LocalDate.format(
    format: DateFormat,
): String = DateTimeFormatter.ofPattern(format.format)
    .format(this.toJavaLocalDate().atTime(0, 0))

actual fun LocalTime.format(
    format: TimeFormat,
): String = DateTimeFormatter.ofPattern(format.format)
    .format(this.toJavaLocalTime())

actual fun DayOfWeek.format(
    format: String,
): String = DateTimeFormatter.ofPattern(format).format(java.time.DayOfWeek.of(this.isoDayNumber))

actual fun Month.format(
    format: String,
): String = DateTimeFormatter.ofPattern(format).format(java.time.Month.of(this.value))
