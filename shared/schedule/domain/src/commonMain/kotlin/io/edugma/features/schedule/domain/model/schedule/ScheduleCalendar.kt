package io.edugma.features.schedule.domain.model.schedule

import io.edugma.features.schedule.domain.model.attentdee.AttendeeType
import io.edugma.features.schedule.domain.model.compact.CompactLessonEvent
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.rrule.Frequency
import io.edugma.features.schedule.domain.model.rrule.RRule
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.properties.Delegates.notNull

class ScheduleCalendar(
    private val compactSchedule: CompactSchedule,
) {
    private val scheduleDays: MutableMap<Int, ScheduleDay> = hashMapOf()
    private var today: LocalDate by notNull()
    var size: Int by notNull()
        private set
    private var todayIndex: Int by notNull()
    private var currentTimeZone: TimeZone by notNull()

    fun init(
        today: LocalDate,
        size: Int,
        todayIndex: Int,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ) {
        this.today = today
        this.size = size
        this.todayIndex = todayIndex
        this.currentTimeZone = timeZone
    }

    private fun getSchedule(date: LocalDate): ScheduleDay {
        val lessons = mutableListOf<LessonEvent>()
        compactSchedule.lessons.forEach { lesson ->
            if (lesson.isToday(date)) {
                lessons.add(lesson.toModel(compactSchedule))
            }
        }
        lessons.sortBy { it.start.dateTime.toInstant(it.start.timeZone) }

        return ScheduleDay(
            isToday = date == today,
            date = date,
            lessons = lessons,
        )
    }

    private fun CompactLessonEvent.isToday(today: LocalDate): Boolean {
        val startDate = start.dateTime.toInstant(start.timeZone)
            .toLocalDateTime(currentTimeZone).date

        if (recurrence.isEmpty() && startDate == today) {
            return true
        }

        return recurrence.any { rRule ->
            rRule.hasMatch(startDate = startDate, date = today)
        }
    }

    private fun RRule.hasMatch(startDate: LocalDate, date: LocalDate): Boolean {
        return when (frequency) {
            Frequency.Daily -> hasDailyMatch(startDate = startDate, date = date)
            Frequency.Weekly -> hasWeeklyMatch(startDate = startDate, date = date)
            Frequency.Monthly -> hasMonthlyMatch(startDate = startDate, date = date)
            Frequency.Yearly -> hasYearlyMatch(startDate = startDate, date = date)
        }
    }

    private fun RRule.hasDailyMatch(startDate: LocalDate, date: LocalDate): Boolean {
        // TODO
        TODO()
    }

    private fun RRule.hasWeeklyMatch(startDate: LocalDate, date: LocalDate): Boolean {
        if (startDate.dayOfWeek != date.dayOfWeek) {
            return false
        }

        // TODO count and others

        val untilDate = this.until?.toLocalDateTime(currentTimeZone)?.date ?: return true

        return date <= untilDate
    }

    private fun RRule.hasMonthlyMatch(startDate: LocalDate, date: LocalDate): Boolean {
        // TODO
        TODO()
    }

    private fun RRule.hasYearlyMatch(startDate: LocalDate, date: LocalDate): Boolean {
        // TODO
        TODO()
    }

    operator fun get(date: LocalDate): ScheduleDay {
        val index = today.daysUntil(date)
        return scheduleDays.getOrPut(index) {
            getSchedule(date)
        }
    }

    operator fun get(index: Int): ScheduleDay {
        return scheduleDays.getOrPut(index) {
            val date = today.plus(DatePeriod(days = index - todayIndex))
            getSchedule(date)
        }
    }

    private fun CompactLessonEvent.toModel(info: CompactSchedule): LessonEvent {
        return LessonEvent(
            id = this.id,
            subject = info.subjects.first { it.id == this.subjectId }.let {
                LessonSubject(
                    id = it.id,
                    title = it.title,
                )
            },
            tags = this.tags,
            teachers = this.attendeesId.mapNotNull { id ->
                info.attendees.first { it.id == id }
                    .takeIf { it.type == AttendeeType.Teacher }
            },
            groups = this.attendeesId.mapNotNull { id ->
                info.attendees.first { it.id == id }
                    .takeIf { it.type == AttendeeType.Group }
            },
            attendees = this.attendeesId.mapNotNull { id ->
                info.attendees.first { it.id == id }
                    .takeIf { it.type != AttendeeType.Group && it.type != AttendeeType.Teacher }
            },
            places = placesId.map { id ->
                val temp = info.places.first { it.id == id }
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
}
