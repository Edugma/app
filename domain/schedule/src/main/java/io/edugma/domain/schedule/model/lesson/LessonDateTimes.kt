package io.edugma.domain.schedule.model.lesson

import io.edugma.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class LessonDateTimes(
    val lesson: Lesson,
    val time: List<LessonDateTime>
): Comparable<LessonDateTimes> {
    override fun compareTo(other: LessonDateTimes): Int {
        return lesson.compareTo(other.lesson)
    }
}

@Serializable
data class LessonDateTime(
    @Serializable(with = LocalDateConverter::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateConverter::class)
    val endDate: LocalDate? = null,
    val time: LessonTime
)
