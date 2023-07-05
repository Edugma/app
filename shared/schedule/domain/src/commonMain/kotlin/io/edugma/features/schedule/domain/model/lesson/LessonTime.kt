package io.edugma.features.schedule.domain.model.lesson

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class LessonTime(
    val start: LocalTime,
    val end: LocalTime,
) : Comparable<LessonTime> {
    override fun compareTo(other: LessonTime): Int {
        val start = this.start.compareTo(other.start)
        return if (start != 0) start else this.end.compareTo(other.end)
    }

    operator fun contains(value: LocalTime): Boolean {
        return value in start..end
    }
}
