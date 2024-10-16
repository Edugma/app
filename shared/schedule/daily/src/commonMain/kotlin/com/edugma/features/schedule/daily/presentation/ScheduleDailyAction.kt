package com.edugma.features.schedule.daily.presentation

import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import kotlinx.datetime.LocalDate

sealed interface ScheduleDailyAction {
    data object OnBack : ScheduleDailyAction
    data object OnFabClick : ScheduleDailyAction
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
        val lesson: LessonEvent,
    ) : ScheduleDailyAction
    data object OnRefresh : ScheduleDailyAction
}
