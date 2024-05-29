package com.edugma.features.schedule.elements.model

import com.edugma.features.schedule.elements.lesson.model.ScheduleEventUiModel
import kotlinx.datetime.LocalDate

data class ScheduleDayUiModel(
    val isToday: Boolean,
    val date: LocalDate,
    val lessons: List<ScheduleEventUiModel>,
)
