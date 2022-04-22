package io.edugma.domain.schedule.model.lesson_type

import kotlinx.serialization.Serializable

@Serializable
data class LessonType(
    val id: String,
    val title: String
)