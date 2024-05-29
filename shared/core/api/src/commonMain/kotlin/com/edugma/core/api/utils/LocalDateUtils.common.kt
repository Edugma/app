package com.edugma.core.api.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.plus

expect fun LocalDateTime.format(
    format: DateTimeFormat = DateTimeFormat.FULL,
): String

expect fun LocalDate.format(
    format: DateFormat = DateFormat.FULL,
): String

expect fun LocalTime.format(
    format: TimeFormat = TimeFormat.HOURS_MINUTES,
): String

expect fun DayOfWeek.format(
    format: String = "EE",
): String

expect fun Month.format(
    format: String = "MMM",
): String
