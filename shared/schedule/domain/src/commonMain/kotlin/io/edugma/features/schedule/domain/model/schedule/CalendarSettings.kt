package io.edugma.features.schedule.domain.model.schedule

import io.edugma.core.api.utils.nowLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

data class CalendarSettings(
    val today: LocalDate,
    val daysCount: Int,
    val weeksCount: Int,
    val todayDayIndex: Int,
    val todayDayOfWeekIndex: Int,
    val todayWeeksIndex: Int,
) {
    companion object {
        val Infinity: CalendarSettings
            get() {
                // TODO replace by MAX_VALUE in 1.7 https://issuetracker.google.com/issues/326887746
                val fakeMaxValue = 10_000
                val weeksCount = fakeMaxValue / 7
                val daysCount = weeksCount * 7

                val today = Clock.System.nowLocalDate()

                val (todayIndex, todayWeeksIndex) = calculateTodayIndex(
                    daysCount = daysCount,
                    today = today,
                )

                return CalendarSettings(
                    daysCount = daysCount,
                    weeksCount = weeksCount,
                    today = today,
                    todayDayIndex = todayIndex,
                    todayDayOfWeekIndex = today.dayOfWeek.isoDayNumber - 1,
                    todayWeeksIndex = todayWeeksIndex,
                )
            }

        private fun calculateTodayIndex(
            daysCount: Int,
            today: LocalDate,
        ): Pair<Int, Int> {
            // Use middle index as pivot to find today index
            val middleIndex = daysCount / 2
            val middleIndexDayOfWeak = middleIndex % 7
            val middleMondayIndex = middleIndex - middleIndexDayOfWeak

            val todayIndexDayOfWeek = today.dayOfWeek.isoDayNumber - 1
            val todayIndex = middleMondayIndex + todayIndexDayOfWeek
            val todayWeeksIndex = todayIndex / 7

            return todayIndex to todayWeeksIndex
        }
    }
}
