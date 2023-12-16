package io.edugma.features.schedule.domain.model.compact

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class LessonDateTime(
    val dateTime: LocalDateTime,
    val timeZone: TimeZone,
)

fun LessonDateTime.zonedTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): LocalTime {
    return dateTime.toInstant(timeZone).toLocalDateTime(timeZone).time
}

fun LessonDateTime.zonedDate(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): LocalDate {
    return dateTime.toInstant(timeZone).toLocalDateTime(timeZone).date
}
