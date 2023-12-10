package io.edugma.features.schedule.domain.model.compact

import io.edugma.features.schedule.domain.model.rrule.RRule
import io.edugma.features.schedule.domain.model.rrule.RRuleSerializer
import kotlinx.serialization.Serializable

@Serializable
data class CompactLessonEvent(
    val id: String,
    val tags: List<String>,
    val subjectId: String,
    val attendeesId: List<String>,
    val placesId: List<String>,
    val start: LessonDateTime,
    val end: LessonDateTime,
    val recurrence: List<@Serializable(RRuleSerializer::class) RRule>,
    val importance: Importance,
)
