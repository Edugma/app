package io.edugma.features.schedule.elements.model

import io.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

class ScheduleCalendarUiModel(
    val scheduleCalendar: ScheduleCalendar,
) {

    fun init(
        today: LocalDate,
        size: Int,
        todayIndex: Int,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ) {
        scheduleCalendar.init(
            today,
            size,
            todayIndex,
            timeZone,
        )
    }

    operator fun get(index: Int): ScheduleDay {
        return scheduleCalendar.get(index)
    }

    operator fun get(date: LocalDate): ScheduleDay {
        return scheduleCalendar.get(date)
    }
}
