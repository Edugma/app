package io.edugma.features.schedule.domain.model.compact

import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CompactLessonFeatures(
    val typeId: String,
    val subjectId: String,
    val teachersId: List<String>,
    val groupsId: List<String>,
    val placesId: List<String>,
)

@Serializable
data class CompactLessonAndTimes(
    val lesson: CompactLessonFeatures,
    val times: List<LessonDateTime>,
)
