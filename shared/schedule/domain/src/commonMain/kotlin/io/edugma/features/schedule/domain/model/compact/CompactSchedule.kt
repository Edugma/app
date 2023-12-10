package io.edugma.features.schedule.domain.model.compact

import io.edugma.features.schedule.domain.model.attentdee.AttendeeInfo
import kotlinx.serialization.Serializable

@Serializable
data class CompactSchedule(
    val lessons: List<CompactLessonEvent>,
    val subjects: List<CompactLessonSubjectInfo>,
    val attendees: List<AttendeeInfo>,
    val places: List<CompactPlaceInfo>,
) {
    companion object {
        val empty by lazy {
            CompactSchedule(
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
            )
        }
    }
}
