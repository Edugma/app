package io.edugma.features.schedule.domain.model.lessonSubject

import io.edugma.features.schedule.domain.model.compact.CompactLessonSubjectInfo
import kotlinx.serialization.Serializable

@Serializable
data class LessonSubject(
    val id: String,
    val title: String,
) : Comparable<LessonSubject> {
    override fun compareTo(other: LessonSubject): Int {
        return title.compareTo(other.title)
    }

    companion object {
        fun from(info: LessonSubjectInfo) =
            LessonSubject(
                id = info.id,
                title = info.title,
            )

        fun from(info: CompactLessonSubjectInfo) =
            LessonSubject(
                id = info.id,
                title = info.title,
            )
    }
}
