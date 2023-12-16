package io.edugma.features.schedule.domain.model.schedule

import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDay(
    val isToday: Boolean,
    val date: LocalDate,
    val lessons: List<LessonEvent>,
)
