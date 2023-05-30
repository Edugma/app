package io.edugma.features.schedule.domain.model.schedule

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDay(
    val date: LocalDate,
    val lessons: List<LessonsByTime>,
)
