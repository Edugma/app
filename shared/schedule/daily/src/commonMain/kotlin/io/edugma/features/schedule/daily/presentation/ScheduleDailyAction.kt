package io.edugma.features.schedule.daily.presentation

import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import kotlinx.datetime.LocalDate

sealed interface ScheduleDailyAction {
    object OnBack : ScheduleDailyAction
    object OnFabClick : ScheduleDailyAction
    data class OnSchedulePosChanged(
        val schedulePos: Int,
    ) : ScheduleDailyAction
    data class OnWeeksPosChanged(
        val weeksPos: Int,
    ) : ScheduleDailyAction
    data class OnDayClick(
        val date: LocalDate,
    ) : ScheduleDailyAction
    data class OnLessonClick(
        val lesson: Lesson,
        val dateTime: LessonDateTime,
    ) : ScheduleDailyAction
    object OnRefreshing : ScheduleDailyAction
}
