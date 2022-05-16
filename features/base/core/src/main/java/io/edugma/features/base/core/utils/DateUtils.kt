package io.edugma.features.base.core.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalDate.format(pattern: String = "dd MMMM yyyy"): String = format(DateTimeFormatter.ofPattern(pattern))

fun LocalTime.format(pattern: String = "HH:mm"): String = format(DateTimeFormatter.ofPattern(pattern, Locale.ROOT))

fun LocalDateTime.format(pattern: String = "dd MMMM yyyy HH:mm"): String = format(DateTimeFormatter.ofPattern(pattern))