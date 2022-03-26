package io.edugma.domain.schedule.model.lesson

import io.edugma.domain.base.utils.converters.LocalTimeConverter
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class LessonTime(
    @Serializable(with = LocalTimeConverter::class)
    val startTime: LocalTime,
    @Serializable(with = LocalTimeConverter::class)
    val endTime: LocalTime
) : Comparable<LessonTime> {
    override fun compareTo(other: LessonTime): Int {
        val start = this.startTime.compareTo(other.startTime)
        if (start == 0) {
            val end = this.endTime.compareTo(other.endTime)
            return end
        }
        return start
    }

    operator fun contains(value: LocalTime): Boolean {
        return value in startTime..endTime
    }

}