package io.edugma.domain.schedule.model.lesson

import kotlinx.serialization.Serializable

@Serializable
data class LessonInfo(
    val lesson: Lesson,
    val dateTime: LessonDateTime
)