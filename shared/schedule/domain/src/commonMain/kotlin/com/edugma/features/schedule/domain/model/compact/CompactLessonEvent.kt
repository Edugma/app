package com.edugma.features.schedule.domain.model.compact

import com.edugma.features.schedule.domain.model.attentdee.AttendeeType
import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import com.edugma.features.schedule.domain.model.place.Place
import com.edugma.features.schedule.domain.model.rrule.RRule
import com.edugma.features.schedule.domain.model.rrule.RRuleSerializer
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

fun CompactLessonEvent.toModel(info: CompactSchedule): LessonEvent {
    return LessonEvent(
        id = this.id,
        subject = info.getSubject(this.subjectId).let {
            LessonSubject(
                id = it.id,
                title = it.title,
            )
        },
        tags = this.tags,
        teachers = this.attendeesId.mapNotNull { id ->
            info.getAttendee(id)
                .takeIf { it.type == AttendeeType.Teacher }
        },
        groups = this.attendeesId.mapNotNull { id ->
            info.getAttendee(id)
                .takeIf { it.type == AttendeeType.Group }
        },
        attendees = this.attendeesId.mapNotNull { id ->
            info.getAttendee(id)
                .takeIf { it.type != AttendeeType.Group && it.type != AttendeeType.Teacher }
        },
        places = placesId.map { id ->
            val temp = info.getPlace(id)
            Place(
                id = temp.id,
                title = temp.title,
                type = temp.getType(),
                description = temp.description.orEmpty(),
            )
        },
        start = this.start,
        end = this.end,
        recurrence = this.recurrence,
        importance = this.importance,
    )
}
