package com.edugma.features.schedule.domain.model.compact

import com.edugma.features.schedule.domain.model.attentdee.AttendeeInfo
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

    fun getSubject(id: String): CompactLessonSubjectInfo {
        return subjects.firstOrNull { it.id == id } ?: error("Subject for $id not found")
    }

    fun getAttendee(id: String): AttendeeInfo {
        return attendees.firstOrNull { it.id == id } ?: error("Attendee for $id not found")
    }

    fun getPlace(id: String): CompactPlaceInfo {
        return places.firstOrNull { it.id == id } ?: error("Place for $id not found")
    }
}
