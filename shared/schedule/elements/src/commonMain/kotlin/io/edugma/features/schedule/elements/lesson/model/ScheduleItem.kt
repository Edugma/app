package io.edugma.features.schedule.elements.lesson.model

import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import kotlinx.datetime.LocalTime

sealed interface ScheduleItem {
    data class LessonEventUiModel(
        val lesson2: LessonEvent,
    ) : ScheduleItem

    data class Window(
        val timeFrom: LocalTime,
        val timeTo: LocalTime,
        val totalMinutes: Long,
    ) : ScheduleItem
}
