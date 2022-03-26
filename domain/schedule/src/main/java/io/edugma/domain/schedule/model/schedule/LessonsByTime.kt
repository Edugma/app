package io.edugma.domain.schedule.model.schedule

import io.edugma.domain.schedule.model.lesson.Lesson
import kotlinx.serialization.Serializable
import io.edugma.domain.schedule.model.lesson.LessonTime

@Serializable
data class LessonsByTime(
    val time: LessonTime,
    val lessons: List<Lesson>
)