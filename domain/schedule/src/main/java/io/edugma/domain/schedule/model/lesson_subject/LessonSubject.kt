package io.edugma.domain.schedule.model.lesson_subject

import kotlinx.serialization.Serializable

@Serializable
data class LessonSubject(
    val id: String,
    val title: String
) : Comparable<LessonSubject> {
    override fun compareTo(other: LessonSubject): Int {
        return title.compareTo(other.title)
    }
}