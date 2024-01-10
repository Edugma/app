package io.edugma.features.schedule.calendar.mapper

import io.edugma.features.schedule.calendar.model.ScheduleCalendarVO
import io.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import io.edugma.features.schedule.domain.model.schedule.ScheduleWeeksCalendar

class CalendarMapper {
    fun map(scheduleCalendar: ScheduleCalendar): ScheduleCalendarVO {
        return scheduleCalendar.toCalendarUiModel()
    }

    private fun ScheduleCalendar.toCalendarUiModel(): ScheduleCalendarVO {
        val weekCalendar = ScheduleWeeksCalendar(this)
        return ScheduleCalendarVO(weekCalendar)
    }
}
