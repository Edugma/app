package io.edugma.features.schedule.domain.model.lesson

import io.edugma.features.schedule.domain.model.attentdee.AttendeeInfo
import io.edugma.features.schedule.domain.model.compact.Importance
import io.edugma.features.schedule.domain.model.compact.LessonDateTime
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.rrule.RRule
import io.edugma.features.schedule.domain.model.rrule.RRuleSerializer
import kotlinx.serialization.Serializable

@Serializable
data class LessonEvent(
    val id: String,
    val tags: List<String>,
    val subject: LessonSubject,
    val attendees: List<AttendeeInfo>,
    val teachers: List<AttendeeInfo>,
    val groups: List<AttendeeInfo>,
    val places: List<Place>,
    val start: LessonDateTime,
    val end: LessonDateTime,
    val recurrence: List<@Serializable(RRuleSerializer::class) RRule>,
    val importance: Importance,
) : Comparable<LessonEvent> {
    override fun compareTo(other: LessonEvent): Int {
        val comparing = subject.compareTo(other.subject)
        return comparing
    }
}
