package io.edugma.features.schedule.daily.model

import io.edugma.core.api.utils.WeekIterator
import io.edugma.core.api.utils.getCeilSunday
import io.edugma.core.api.utils.getFloorMonday
import io.edugma.core.api.utils.nowLocalDate
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

data class WeekUiModel(
    val days: List<DayUiModel>,
) {
    companion object {
        fun fromSchedule(
            schedule: List<ScheduleDayUiModel>,
            today: LocalDate = Clock.System.nowLocalDate(),
        ): List<WeekUiModel> {
            val dateFrom = schedule.firstOrNull()?.date?.getFloorMonday()
            val dateTo = schedule.lastOrNull()?.date?.getCeilSunday()

            if (dateFrom == null || dateTo == null) return emptyList()
            return WeekIterator(dateFrom, dateTo).map {
                WeekUiModel(
                    it.map { date ->
                        DayUiModel(
                            date,
                            date == today,
                            schedule.firstOrNull { it.date == date }?.lessons
                                ?.filterIsInstance<ScheduleItem.LessonByTime>()
                                ?.size ?: 0,
                        )
                    },
                )
            }
        }
    }
}
