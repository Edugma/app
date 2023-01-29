package io.edugma.features.schedule.domain.model.lesson

import io.edugma.features.schedule.domain.model.group.Group
import io.edugma.features.schedule.domain.model.lesson_subject.LessonSubject
import io.edugma.features.schedule.domain.model.lesson_type.LessonType
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.teacher.Teacher
import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val subject: LessonSubject,
    val type: LessonType,
    val teachers: List<Teacher>,
    val groups: List<Group>,
    val places: List<Place>,
) : Comparable<Lesson> {
    override fun compareTo(other: Lesson): Int {
        val comparing = subject.compareTo(other.subject)
        return if (comparing != 0) {
            comparing
        } else {
            type.title.compareTo(other.type.title)
        }
    }
}
