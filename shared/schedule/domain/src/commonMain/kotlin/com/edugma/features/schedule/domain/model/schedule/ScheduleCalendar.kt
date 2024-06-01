package com.edugma.features.schedule.domain.model.schedule

import co.touchlab.kermit.Logger
import com.edugma.features.schedule.domain.model.compact.CompactLessonEvent
import com.edugma.features.schedule.domain.model.compact.CompactSchedule
import com.edugma.features.schedule.domain.model.compact.toModel
import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.domain.model.rrule.Frequency
import com.edugma.features.schedule.domain.model.rrule.RRule
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
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

        // Sort by today's time
        lessons.sortBy {
            it.start.dateTime.toInstant(it.start.timeZone)
                .toLocalDateTime(currentTimeZone).time
        }

        return ScheduleDay(
            isToday = date == today,
            date = date,
            lessons = lessons,
        )
    }

    private fun CompactLessonEvent.isToday(today: LocalDate): Boolean {
        val startDate = start.dateTime.toInstant(start.timeZone)
            .toLocalDateTime(currentTimeZone).date

        // If the event is a one-time and it is today
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
        // Check for startDate
        if (date < startDate) return false

        // Check for untilDate
        this.until?.let {
            if (date > this.until.date) return false
        }

        // Check for count
        if (count != 0 && date > getEndDateByCount(startDate)) return false

        // TODO Check for interval (2+)

        // TODO Check for by*

        return true
    }

    private fun RRule.hasWeeklyMatch(startDate: LocalDate, date: LocalDate): Boolean {
        // Check for startDate
        if (date < startDate) return false

        // No events with same day of week
        if (startDate.dayOfWeek != date.dayOfWeek) return false

        // Check for untilDate
        this.until?.let {
            if (date > this.until.date) return false
        }

        // Check for count
        if (count != 0 && date > getEndDateByCount(startDate)) return false

        // TODO Check for interval (2+)

        // TODO Check for by*

        return true
    }

    private fun RRule.hasMonthlyMatch(startDate: LocalDate, date: LocalDate): Boolean {
        // Check for startDate
        if (date < startDate) return false

        // No events with same moth
        if (startDate.dayOfMonth != date.dayOfMonth) return false

        // Check for untilDate
        this.until?.let {
            if (date > this.until.date) return false
        }

        // Check for count
        if (count != 0 && date > getEndDateByCount(startDate)) return false

        // TODO Check for interval (2+)

        // TODO Check for by*

        return true
    }

    private fun RRule.hasYearlyMatch(startDate: LocalDate, date: LocalDate): Boolean {
        // Check for startDate
        if (date < startDate) return false

        // No events with same moth
        if (startDate.dayOfYear != date.dayOfYear) return false

        // Check for untilDate
        this.until?.let {
            if (date > this.until.date) return false
        }

        // Check for count
        if (count != 0 && date > getEndDateByCount(startDate)) return false

        // TODO Check for interval (2+)

        // TODO Check for by*

        return true
    }

    private fun RRule.getEndDateByCount(startDate: LocalDate): LocalDate {
        if (count <= 1) return startDate

        val unit = when (this.frequency) {
            Frequency.Daily -> DateTimeUnit.DAY
            Frequency.Weekly -> DateTimeUnit.WEEK
            Frequency.Monthly -> DateTimeUnit.MONTH
            Frequency.Yearly -> DateTimeUnit.YEAR
        }

        return startDate.plus(this.count - 1, unit)
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

    fun findEvent(id: String): LessonEvent? {
        return compactSchedule.lessons
            .firstOrNull { it.id == id }
            ?.toModel(compactSchedule)
    }

    fun getEventCount(
        date: LocalDate,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): Int {
        this.currentTimeZone = timeZone
        return compactSchedule.lessons.count { lesson -> lesson.isToday(date) }
    }
}
