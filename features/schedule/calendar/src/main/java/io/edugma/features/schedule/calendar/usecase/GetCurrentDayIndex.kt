package io.edugma.features.schedule.calendar.usecase

import io.edugma.features.schedule.calendar.model.CalendarScheduleVO
import java.time.LocalDate

class GetCurrentDayIndex {
    operator fun invoke(schedule: List<CalendarScheduleVO>): Pair<Int, Int> {
        val currentDay = LocalDate.now()
        var notBeforeLessons = false

        val index = schedule.indexOfFirst {
            val lastDayOfWeek = it.weekSchedule.lastOrNull() ?: return@indexOfFirst false

            if (currentDay > lastDayOfWeek.date) return@indexOfFirst false

            val firstDayOfWeek = it.weekSchedule.firstOrNull() ?: return@indexOfFirst false

            if (currentDay < firstDayOfWeek.date) return@indexOfFirst false

            notBeforeLessons = true

            return@indexOfFirst true
        }

        return when {
            index != -1 -> index to getCurrentDayOfWeek(currentDay, schedule[index])
            schedule.isEmpty() -> 0 to -1
            notBeforeLessons -> schedule.lastIndex to -1
            else -> 0 to -1
        }
    }

    private fun getCurrentDayOfWeek(
        currentDay: LocalDate,
        calendarScheduleVO: CalendarScheduleVO,
    ): Int {
        return calendarScheduleVO.weekSchedule.indexOfFirst { it.date == currentDay }
    }
}
