package io.edugma.features.schedule.daily.model

import io.edugma.domain.base.utils.WeekIterator
import io.edugma.domain.base.utils.getCeilSunday
import io.edugma.domain.base.utils.getFloorMonday
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import java.time.LocalDate

data class WeekUiModel(
    val days: List<DayUiModel>
) {
    companion object {
        fun fromSchedule(
            schedule: List<ScheduleDayUiModel>,
            today: LocalDate = LocalDate.now()
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
                                ?.size ?: 0
                        )
                    }
                )
            }
        }
    }
}