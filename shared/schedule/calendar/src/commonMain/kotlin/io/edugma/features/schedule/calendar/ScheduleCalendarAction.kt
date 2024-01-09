package io.edugma.features.schedule.calendar

import kotlinx.datetime.LocalDate

sealed interface ScheduleCalendarAction {
    data class OnDayClick(val date: LocalDate) : ScheduleCalendarAction
}
