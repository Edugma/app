package io.edugma.features.schedule.calendar.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class CalendarDayVO(
    val dayTitle: String,
    val date: LocalDate,
    val lessons: List<CalendarLessonPlaceVO>,
)
