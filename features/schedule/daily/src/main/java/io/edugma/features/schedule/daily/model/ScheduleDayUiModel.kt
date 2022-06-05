package io.edugma.features.schedule.daily.model

import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import java.time.LocalDate

data class ScheduleDayUiModel(
    val date: LocalDate,
    val lessons: List<ScheduleItem>
)