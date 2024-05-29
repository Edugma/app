package com.edugma.core.api.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

fun LocalDate.getFloorMonday(): LocalDate {
    val currentValue = this.dayOfWeek.isoDayNumber
    val mondayValue = DayOfWeek.MONDAY.isoDayNumber
    return this.plus(mondayValue - currentValue, DateTimeUnit.DAY)
}

fun LocalDate.getCeilSunday(): LocalDate {
    val currentValue = this.dayOfWeek.isoDayNumber
    val sunday = DayOfWeek.SUNDAY.isoDayNumber
    return this.plus(sunday - currentValue, DateTimeUnit.DAY)
}

class DayIterator(
    private val dateFrom: LocalDate,
    private val dateTo: LocalDate,
) : Iterable<LocalDate> {
    override fun iterator() = Iterator()
    inner class Iterator : kotlin.collections.Iterator<LocalDate> {
        var currentDate: LocalDate? = null

        override fun hasNext(): Boolean {
            val currentDate = currentDate ?: dateFrom
            return currentDate.daysUntil(dateTo) > 0
        }

        override fun next(): LocalDate {
            val newDate = currentDate?.let { it.plus(1, DateTimeUnit.DAY) } ?: dateFrom
            this.currentDate = newDate
            return newDate
        }
    }
}

class WeekIterator(
    private val firstMonday: LocalDate,
    private val lastSunday: LocalDate,
) : Iterable<DayIterator> {
    private val totalWeeks = (firstMonday.daysUntil(lastSunday) + 1) / 7

    override fun iterator() = Iterator()
    inner class Iterator : kotlin.collections.Iterator<DayIterator> {
        var currentWeek: Int? = null

        override fun hasNext(): Boolean {
            val currentWeek = currentWeek ?: 0
            return currentWeek <= totalWeeks
        }

        override fun next(): DayIterator {
            val newWeek = currentWeek?.let { it + 1 } ?: 0
            currentWeek = newWeek
            val monday = firstMonday.plus(newWeek * 7L, DateTimeUnit.DAY)
            val sunday = monday.plus(6L, DateTimeUnit.DAY)
            return DayIterator(
                monday,
                sunday,
            )
        }
    }
}

fun Clock.System.nowLocalTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalTime {
    return now().toLocalDateTime(timeZone).time
}

fun Clock.System.nowLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return todayIn(timeZone)
}

fun Instant.toCurrentLocalDateTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): LocalDateTime {
    return toLocalDateTime(timeZone)
}

fun Clock.System.nowLocalDateTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): LocalDateTime {
    return now().toLocalDateTime(timeZone)
}

fun LocalDate.isLeapYear(): Boolean = isLeapYear(this.year)
fun LocalDateTime.isLeapYear(): Boolean = isLeapYear(this.year)

internal fun isLeapYear(year: Int): Boolean {
    val prolepticYear: Long = year.toLong()
    return prolepticYear and 3 == 0L && (prolepticYear % 100 != 0L || prolepticYear % 400 == 0L)
}

fun LocalDateTime.formatTime(
    format: TimeFormat = TimeFormat.HOURS_MINUTES,
): String = this.time.format(format)

fun LocalDateTime.formatDate(
    format: DateFormat = DateFormat.FULL,
): String = this.date.format(format)
