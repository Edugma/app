package io.edugma.features.schedule.domain.model.review

import io.edugma.features.schedule.domain.model.compact.CompactLessonEvent
import io.edugma.features.schedule.domain.model.compact.CompactLessonSubjectInfo
import kotlinx.serialization.Serializable

@Serializable
data class LessonTimesReview(
    val subject: CompactLessonSubjectInfo,
    val events: List<CompactLessonEvent>,
)
