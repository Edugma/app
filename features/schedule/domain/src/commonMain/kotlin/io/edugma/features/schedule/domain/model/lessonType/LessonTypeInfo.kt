package io.edugma.features.schedule.domain.model.lessonType

import kotlinx.serialization.Serializable

@Serializable
data class LessonTypeInfo(
    val id: String,
    val title: String,
    val shortTitle: String,
    val description: String,
    val isImportant: Boolean,
)
