package io.edugma.domain.base.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

actual fun LocalDateTime.format(
    format: String,
): String = DateTimeFormatter.ofPattern(format).format(this.toJavaLocalDateTime())

actual fun LocalDate.format(
    format: String,
): String = DateTimeFormatter.ofPattern(format).format(this.toJavaLocalDate().atTime(0, 0))

actual fun LocalTime.format(
    format: String,
): String = DateTimeFormatter.ofPattern(format).format(this.toJavaLocalTime())

actual fun DayOfWeek.format(
    format: String,
): String = DateTimeFormatter.ofPattern(format).format(java.time.DayOfWeek.of(this.isoDayNumber))

actual fun Month.format(
    format: String,
): String = DateTimeFormatter.ofPattern(format).format(java.time.Month.of(this.value))
