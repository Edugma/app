package com.edugma.features.schedule.daily.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class DayUiModel(
    val date: LocalDate,
    val isToday: Boolean,
    val lessonCount: Int,
)
