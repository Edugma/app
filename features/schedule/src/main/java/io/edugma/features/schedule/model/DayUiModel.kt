package io.edugma.features.schedule.model

import java.time.LocalDate

data class DayUiModel(
    val date: LocalDate,
    val isToday: Boolean,
    val lessonCount: Int
)