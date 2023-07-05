package io.edugma.features.schedule.calendar.model

import androidx.compose.runtime.Immutable

@Immutable
data class CalendarLessonPlaceVO(
    val time: String,
    val lessons: List<CalendarLessonVO>,
)
