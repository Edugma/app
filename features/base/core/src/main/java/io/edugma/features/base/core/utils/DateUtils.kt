package io.edugma.features.base.core.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDate.format(pattern: String = "dd MMMM yyyy"): String = format(DateTimeFormatter.ofPattern(pattern))

fun LocalDateTime.format(pattern: String = "dd.LLLL.yyyy mm:hh"): String = format(DateTimeFormatter.ofPattern(pattern))