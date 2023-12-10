package io.edugma.features.schedule.domain.model.rrule

import kotlinx.datetime.Instant

/**
 * @param frequency (required)
 * @param weekStartDay The week start day. Must be one of the RRule.MO, RRule.TU, RRule.WE
 * constants, or an integer, specifying the first day of the week. This will affect recurrences
 * based on weekly periods. The default week start is RRule.MO.
 * @param until If given, this must be a Date instance, that will specify the limit
 * of the recurrence. If a recurrence instance happens to be the same as the Date
 * instance given in the until argument, this will be the last occurrence.
 * @param count How many occurrences will be generated.
 * @param interval The interval between each freq iteration. For example,
 * when using RRule.YEARLY, an interval of 2 means once every two years,
 * but with RRule.HOURLY, it means once every two hours. The default interval is 1.
 * @param byMonth in +/-[1-12], the months to apply the recurrence to.
 * @param byMonthDay in +/-[1-31], the month days to apply the recurrence to.
 * @param byYearDay in +/-[1-366], the year days to apply the recurrence to.
 * @param byWeekNumber in +/-[1-53], the week numbers to apply the recurrence to.
 * Week numbers have the meaning described in ISO8601, that is,
 * the first week of the year is that containing at least four days of the new year.
 * @param byWeekday If given, it must be either an integer (0 == RRule.MO),
 * an array of integers, one of the weekday constants (RRule.MO, RRule.TU, etc),
 * or an array of these constants. When given, these variables will define the weekdays
 * where the recurrence will be applied. It's also possible to use
 * an argument n for the weekday instances, which will mean the nth occurrence of this weekday
 * in the period. For example, with RRule.MONTHLY, or with RRule.YEARLY and BYMONTH,
 * using RRule.FR.nth(+1) or RRule.FR.nth(-1) in [byWeekday] will specify the first or
 * last friday of the month where the recurrence happens. Notice that the RFC documentation,
 * this is specified as BYDAY, but was renamed to avoid the ambiguity of that argument.
 * @param bySetPos in +/-[1-366]. Each given integer will specify an occurrence number,
 * corresponding to the nth occurrence of the rule inside the frequency period.
 * For example, a [bySetPos] of -1 if combined with a RRule.MONTHLY frequency,
 * and a [byWeekday] of (RRule.MO, RRule.TU, RRule.WE, RRule.TH, RRule.FR),
 * will result in the last work day of every month.
 */
data class RRule(
    val frequency: Frequency,
    val weekStartDay: Weekday? = null,
    val until: Instant? = null,
    val count: Int = 0,
    val interval: Int = 0,
    val byWeekday: List<WeekdayNum> = emptyList(),
    val byMonth: List<Int> = emptyList(),
    val byMonthDay: List<Int> = emptyList(),
    val byWeekNumber: List<Int> = emptyList(),
    val byYearDay: List<Int> = emptyList(),
    val bySetPos: List<Int> = emptyList(),
)
