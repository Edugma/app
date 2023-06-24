package io.edugma.features.schedule.domain.model.lesson

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class LessonDateTimes(
    val lesson: Lesson,
    val time: List<LessonDateTime>,
) : Comparable<LessonDateTimes> {
    override fun compareTo(other: LessonDateTimes): Int {
        return lesson.compareTo(other.lesson)
    }
}

@Serializable
data class LessonDateTime(
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val time: LessonTime,
)
