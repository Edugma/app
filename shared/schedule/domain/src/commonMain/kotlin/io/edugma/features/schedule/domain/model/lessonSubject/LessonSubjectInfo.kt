package io.edugma.features.schedule.domain.model.lessonSubject

import kotlinx.serialization.Serializable

@Serializable
data class LessonSubjectInfo(
    val id: String,
    val title: String,
) : Comparable<LessonSubjectInfo> {
    override fun compareTo(other: LessonSubjectInfo): Int {
        return title.compareTo(other.title)
    }
}
