package com.edugma.features.schedule.domain.model.rrule

import com.edugma.core.api.utils.isMax
import com.edugma.core.api.utils.isMin
import com.edugma.features.schedule.domain.model.rrule.RRuleFormatter.DateParser.parseRRuleInstant
import com.edugma.features.schedule.domain.model.rrule.RRuleFormatter.DateParser.toRruleString
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

open class RRuleFormatter() {
    private val name = "RRULE"

    var freq: Frequency = Frequency.Daily

    var wkst: Weekday? = null
    var until: UntilDateTime? = null
    var count = 0
    var interval = 1

    val byDay = arrayListOf<WeekdayNum>()
    val byMonth = arrayListOf<Int>() // in +/-[1-12]
    val byMonthDay = arrayListOf<Int>() // in +/-[1-31]
    val byWeekNo = arrayListOf<Int>() // in +/-[1-53]
    val byYearDay = arrayListOf<Int>() // in +/-[1-366]
    val bySetPos = arrayListOf<Int>() // in +/-[1-366]

    fun toRRule(): RRule {
        return RRule(
            frequency = freq,
            weekStartDay = wkst,
            until = until,
            count = count,
            interval = interval,
            byWeekday = byDay,
            byMonth = byMonth,
            byMonthDay = byMonthDay,
            byWeekNumber = byWeekNo,
            byYearDay = byYearDay,
            bySetPos = bySetPos,
        )
    }

    constructor(rRule: RRule) : this() {
        freq = rRule.frequency
        wkst = rRule.weekStartDay
        until = rRule.until
        count = rRule.count
        interval = rRule.interval
        byDay.addAll(rRule.byWeekday)
        byMonth.addAll(rRule.byMonth)
        byMonthDay.addAll(rRule.byMonthDay)
        byWeekNo.addAll(rRule.byWeekNumber)
        byYearDay.addAll(rRule.byYearDay)
        bySetPos.addAll(rRule.bySetPos)
    }

    constructor(rfc5545String: String) : this() {
        val components = rfc5545String.replace("$name:", "").split(";", "=")
        var i = 0
        while (i < components.size) {
            val component = components[i]
            if (component == "FREQ") {
                i += 1
                freq =
                    when (components[i]) {
                        "DAILY" -> Frequency.Daily
                        "WEEKLY" -> Frequency.Weekly
                        "MONTHLY" -> Frequency.Monthly
                        "YEARLY" -> Frequency.Yearly
                        else -> Frequency.Daily
                    }
            }
            if (component == "INTERVAL") {
                i += 1
                interval = components[i].toIntOrNull() ?: 1
            }
            if (component == "BYDAY") {
                i += 1
                val dayStrings = components[i].split(",")
                for (dayString in dayStrings) {
                    val weekDay = weekDayFromString(dayString)

                    if (weekDay != null) {
                        if (dayString.length > 2) {
                            val number = dayString.replace(Regex("[^-?0-9]+"), "").toIntOrNull() ?: 0
                            byDay.add(WeekdayNum(number, weekDay))
                        } else {
                            byDay.add(WeekdayNum(0, weekDay))
                        }
                    }
                }
            }

            if (component == "BYMONTHDAY") {
                i += 1
                val dayStrings = components[i].split(",")
                for (dayString in dayStrings) {
                    val monthDay = dayString.toIntOrNull()
                    if (monthDay != null) {
                        byMonthDay.add(monthDay)
                    }
                }
            }

            if (component == "BYMONTH") {
                i += 1
                val monthStrings = components[i].split(",")
                for (monthString in monthStrings) {
                    val month = monthString.toIntOrNull()
                    if (month != null) {
                        byMonth.add(month)
                    }
                }
            }

            if (component == "BYWEEKNO") {
                i += 1
                val weekStrings = components[i].split(",")
                for (weekString in weekStrings) {
                    val week = weekString.toIntOrNull()
                    if (week != null) {
                        byWeekNo.add(week)
                    }
                }
            }

            if (component == "BYYEARDAY") {
                i += 1
                val dayStrings = components[i].split(",")
                for (dayString in dayStrings) {
                    val yearDay = dayString.toIntOrNull()
                    if (yearDay != null) {
                        byYearDay.add(yearDay)
                    }
                }
            }

            if (component == "BYSETPOS") {
                i += 1
                val posStrings = components[i].split(",")
                for (posString in posStrings) {
                    val pos = posString.toIntOrNull()
                    if (pos != null) {
                        bySetPos.add(pos)
                    }
                }
            }

            if (component == "COUNT") {
                i += 1
                count = components[i].toIntOrNull() ?: 1
            } else if (component == "UNTIL") {
                i += 1
                until = parseRRuleInstant(components[i])
            }

            if (component == "WKST") {
                i += 1
                wkst = weekDayFromString(components[i])
            }
            i++
        }
    }

    /**
     * Transforms this RRule to a RFC5545 standard iCal String.
     */
    fun toRFC5545String(): String {
        val buf = StringBuilder()
        buf.append("$name:")
        buf.append("FREQ=").append(freq.toString())
        if (interval > 1) {
            buf.append(";INTERVAL=").append(interval)
        }
        if (until != null) {
            buf.append(";UNTIL=").append(until!!.toInstant().toRruleString())
        }
        if (count > 0) {
            buf.append(";COUNT=").append(count)
        }
        if (byYearDay.isNotEmpty()) {
            buf.append(";BYYEARDAY=")
            writeIntList(byYearDay, buf)
        }
        if (byMonth.isNotEmpty()) {
            buf.append(";BYMONTH=")
            writeIntList(byMonth, buf)
        }
        if (byMonthDay.isNotEmpty()) {
            buf.append(";BYMONTHDAY=")
            writeIntList(byMonthDay, buf)
        }
        if (byWeekNo.isNotEmpty()) {
            buf.append(";BYWEEKNO=")
            writeIntList(byWeekNo, buf)
        }
        if (byDay.isNotEmpty()) {
            buf.append(";BYDAY=")
            var first = true
            for (day in byDay) {
                if (!first) {
                    buf.append(',')
                } else {
                    first = false
                }
                buf.append(day.toICalString())
            }
        }
        if (bySetPos.isNotEmpty()) {
            buf.append(";BYSETPOS=")
            writeIntList(bySetPos, buf)
        }
        if (wkst != null) {
            buf.append(";WKST=").append(wkst?.initials)
        }

        return buf.toString()
    }

    private fun writeIntList(
        integers: List<Int>,
        out: StringBuilder,
    ) {
        for (i in integers.indices) {
            if (0 != i) {
                out.append(',')
            }
            out.append(integers[i])
        }
    }

    private fun weekDayFromString(dayString: String): Weekday? {
        return when {
            dayString.contains("SU") -> Weekday.Sunday
            dayString.contains("MO") -> Weekday.Monday
            dayString.contains("TU") -> Weekday.Tuesday
            dayString.contains("WE") -> Weekday.Wednesday
            dayString.contains("TH") -> Weekday.Thursday
            dayString.contains("FR") -> Weekday.Friday
            dayString.contains("SA") -> Weekday.Saturday
            else -> null
        }
    }

    object DateParser {
        /**
         * Supported format yyyyMMdd'T'HHmmss'Z'
         */
        fun parseRRuleInstant(rruleDate: String): UntilDateTime {
            val lastIndex = rruleDate.lastIndex

            check(rruleDate[lastIndex] == 'Z')
            val seconds = rruleDate.toPositive2Int(lastIndex - 2)
            val minutes = rruleDate.toPositive2Int(lastIndex - 4)
            val hours = rruleDate.toPositive2Int(lastIndex - 6)
            check(rruleDate[lastIndex - 7] == 'T')
            val days = rruleDate.toPositive2Int(lastIndex - 9)
            val months = rruleDate.toPositive2Int(lastIndex - 11)
            val years = rruleDate.toPositiveIntYear()

            val time = LocalTime(
                hour = hours,
                minute = minutes,
                second = seconds,
            )

            val date = LocalDate(
                year = years,
                monthNumber = months,
                dayOfMonth = days,
            ).let {
                if (time.isMin) {
                    it.minus(DatePeriod(days = 1))
                } else {
                    it
                }
            }

            return UntilDateTime(
                date = date,
                time = time.takeIf { !it.isMax && !it.isMin },
            )
        }

        private fun String.toPositive2Int(startIndex: Int): Int {
            return this[startIndex].digitToInt() * 10 +
                this[startIndex + 1].digitToInt()
        }

        private fun String.toPositiveIntYear(): Int {
            return this[0].digitToInt() * 1000 +
                this[1].digitToInt() * 100 +
                this[2].digitToInt() * 10 +
                this[3].digitToInt()
        }

        /**
         * Return rrule formatted date yyyyMMdd'T'HHmmss'Z'
         */
        fun Instant.toRruleString(): String {
            val date = this.toLocalDateTime(TimeZone.UTC)

            return buildString {
                append4Pad(date.year)
                append2Pad(date.monthNumber)
                append2Pad(date.dayOfMonth)
                append('T')
                append2Pad(date.hour)
                append2Pad(date.minute)
                append2Pad(date.second)
                append('Z')
            }
        }

        private fun StringBuilder.append2Pad(value: Int) {
            if (value < 10) {
                append('0')
                append(value.digitToChar())
            } else {
                append((value / 10).digitToChar())
                append((value % 10).digitToChar())
            }
        }

        private fun StringBuilder.append4Pad(value: Int) {
            when {
                value >= 1000 -> {
                    append((value / 1000).digitToChar())
                    append(((value / 100) % 10).digitToChar())
                    append(((value / 10) % 10).digitToChar())
                    append((value % 10).digitToChar())
                }
                value >= 100 -> {
                    append('0')
                    append(((value / 100) % 10).digitToChar())
                    append(((value / 10) % 10).digitToChar())
                    append((value % 10).digitToChar())
                }
                else -> {
                    append2Pad(value)
                }
            }
        }
    }
}
