package io.edugma.features.schedule.calendar.model

import io.edugma.domain.base.utils.getCeilSunday
import io.edugma.domain.base.utils.getFloorMonday
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import java.time.temporal.ChronoUnit

data class ScheduleCalendarWeek(
    val weekNumber: Int,
    val schedule: List<ScheduleDay>,
)

internal fun List<ScheduleDay>.toCalendarUiModel(): List<ScheduleCalendarWeek> {
    if (isEmpty()) return emptyList()

    var res = asSequence()

    val firstItem = first()
    val floorMonday = firstItem.date.getFloorMonday()

    if (floorMonday != firstItem.date) {
        val daysToAdd = floorMonday.until(firstItem.date, ChronoUnit.DAYS)
        res = sequence {
            (0 until daysToAdd).forEach {
                val date = floorMonday.plusDays(it)
                yield(
                    ScheduleDay(
                        date = date,
                        lessons = emptyList(),
                    ),
                )
            }
        } + res
    }

    val lastItem = last()
    val ceilSunday = lastItem.date.getCeilSunday()

    if (ceilSunday != lastItem.date) {
        val daysToAdd = lastItem.date.until(ceilSunday, ChronoUnit.DAYS)
        res += sequence {
            (1..daysToAdd).forEach {
                val date = lastItem.date.plusDays(it)
                yield(
                    ScheduleDay(
                        date = date,
                        lessons = emptyList(),
                    ),
                )
            }
        }
    }

    return res.windowed(7, 7)
        .mapIndexed { index, list ->
            ScheduleCalendarWeek(
                weekNumber = index,
                schedule = list,
            )
        }.toList()
}
