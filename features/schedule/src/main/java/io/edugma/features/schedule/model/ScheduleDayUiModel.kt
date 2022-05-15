package io.edugma.features.schedule.model

import java.time.LocalDate

data class ScheduleDayUiModel(
    val date: LocalDate,
    val lessons: List<ScheduleItem>
)