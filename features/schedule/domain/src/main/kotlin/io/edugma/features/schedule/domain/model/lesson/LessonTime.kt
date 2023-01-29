package io.edugma.features.schedule.domain.model.lesson

import io.edugma.domain.base.utils.converters.LocalTimeConverter
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class LessonTime(
    @Serializable(with = LocalTimeConverter::class)
    val start: LocalTime,
    @Serializable(with = LocalTimeConverter::class)
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
