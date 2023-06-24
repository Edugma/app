package io.edugma.features.schedule.domain.model.lessonType

import kotlinx.serialization.Serializable

@Serializable
data class LessonType(
    val id: String,
    val title: String,
    val isImportant: Boolean,
) {
    companion object {
        fun from(info: LessonTypeInfo) =
            LessonType(
                id = info.id,
                title = info.title,
                isImportant = info.isImportant,
            )
    }
}
