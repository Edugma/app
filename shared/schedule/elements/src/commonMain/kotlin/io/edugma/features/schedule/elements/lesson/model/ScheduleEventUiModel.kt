package io.edugma.features.schedule.elements.lesson.model

import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import kotlinx.datetime.LocalTime

sealed interface ScheduleEventUiModel {
    data class Lesson(
        val lesson: LessonEvent,
    ) : ScheduleEventUiModel

    data class Window(
        val timeFrom: LocalTime,
        val timeTo: LocalTime,
        val totalMinutes: Long,
    ) : ScheduleEventUiModel
}
