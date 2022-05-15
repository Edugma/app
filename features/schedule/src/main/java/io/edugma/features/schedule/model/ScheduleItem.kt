package io.edugma.features.schedule.model

import io.edugma.domain.schedule.model.schedule.LessonsByTime
import java.time.LocalTime

sealed interface ScheduleItem {
    data class LessonByTime(
        val lesson: LessonsByTime
    ) : ScheduleItem

    data class Window(
        val timeFrom: LocalTime,
        val timeTo: LocalTime,
        val totalMinutes: Long
    ) : ScheduleItem
}