package com.edugma.features.schedule.calendar.model

import androidx.compose.runtime.Immutable
import com.edugma.core.api.utils.TimeFormat
import com.edugma.core.api.utils.format
import com.edugma.features.schedule.domain.model.compact.Importance
import com.edugma.features.schedule.domain.model.compact.zonedTime
import com.edugma.features.schedule.domain.model.lesson.LessonEvent

@Immutable
data class CalendarLessonVO(
    val time: String,
    val title: String,
    val importance: Importance,
)

fun LessonEvent.toCalendarLesson(): CalendarLessonVO {
    val timeStart = this.start.zonedTime()
    val timeEnd = this.start.zonedTime()
    val timeFrom = timeStart.format(TimeFormat.HOURS_MINUTES)
    val timeTo = timeEnd.format(TimeFormat.HOURS_MINUTES)
    val timeString = "$timeFrom - $timeTo"
    return CalendarLessonVO(
        time = timeString,
        importance = importance,
        title = subject.title,
    )
}
