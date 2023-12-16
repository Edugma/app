package io.edugma.features.schedule.domain.model.schedule

import kotlinx.datetime.plus

class ScheduleWeeksCalendar(
    private val scheduleCalendar: ScheduleCalendar,
) {
    val size: Int
        get() = scheduleCalendar.size / 7

    fun getWeek(index: Int): List<ScheduleDay> {
        val mondayIndex = index * 7

        return List(7) { dayOfWeekIndex ->
            scheduleCalendar.get(mondayIndex + dayOfWeekIndex)
        }
    }
}
