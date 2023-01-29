package io.edugma.features.schedule.domain.model.schedule

import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import kotlinx.serialization.Serializable

@Serializable
data class LessonsByTime(
    val time: LessonTime,
    val lessons: List<Lesson>,
)
