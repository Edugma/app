package io.edugma.features.schedule.domain.model.review

import io.edugma.domain.base.utils.converters.LocalDateConverter
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.lesson_subject.LessonSubject
import io.edugma.features.schedule.domain.model.lesson_type.LessonType
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate

@Serializable
data class LessonTimesReview(
    val subject: LessonSubject,
    val days: List<LessonTimesReviewByType>,
)

@Serializable
data class LessonTimesReviewByType(
    val lessonType: LessonType,
    val days: List<LessonReviewUnit>,
) : Comparable<LessonTimesReviewByType> {
    override operator fun compareTo(other: LessonTimesReviewByType): Int {
        return this.lessonType.title.compareTo(other.lessonType.title)
    }
}

@Serializable
data class LessonReviewUnit(
    val dayOfWeek: DayOfWeek,
    val time: List<LessonTime>,
    val dates: List<LessonDates>,
) : Comparable<LessonReviewUnit> {
    override operator fun compareTo(other: LessonReviewUnit): Int {
        val thisFirstDate = dates.firstOrNull()
        val otherFirstDate = other.dates.firstOrNull()

        if (thisFirstDate == null || otherFirstDate == null) {
            return dayOfWeek.compareTo(other.dayOfWeek)
        }

        return thisFirstDate.compareTo(otherFirstDate)
    }
}

@Serializable
data class LessonDates(
    @Serializable(with = LocalDateConverter::class)
    val start: LocalDate,
    @Serializable(with = LocalDateConverter::class)
    val end: LocalDate?,
) : Comparable<LessonDates> {
    override fun compareTo(other: LessonDates): Int {
        return start.compareTo(other.start)
    }
}
