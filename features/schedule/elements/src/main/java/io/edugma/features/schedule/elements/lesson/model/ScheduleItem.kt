package io.edugma.features.schedule.elements.lesson.model

import io.edugma.features.schedule.domain.model.schedule.LessonsByTime
import java.time.LocalTime

sealed interface ScheduleItem {
    data class LessonByTime(
        val lesson: LessonsByTime,
    ) : ScheduleItem

    data class Window(
        val timeFrom: LocalTime,
        val timeTo: LocalTime,
        val totalMinutes: Long,
    ) : ScheduleItem
}
