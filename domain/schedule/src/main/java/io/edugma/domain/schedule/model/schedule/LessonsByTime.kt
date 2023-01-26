package io.edugma.domain.schedule.model.schedule

import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonTime
import kotlinx.serialization.Serializable

@Serializable
data class LessonsByTime(
    val time: LessonTime,
    val lessons: List<Lesson>,
)
