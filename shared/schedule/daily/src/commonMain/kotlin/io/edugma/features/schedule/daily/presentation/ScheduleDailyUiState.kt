package io.edugma.features.schedule.daily.presentation

import io.edugma.core.api.utils.nowLocalDate
import io.edugma.features.schedule.daily.model.ScheduleWeeksUiModel
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.schedule.ScheduleWeeksCalendar
import io.edugma.features.schedule.elements.model.ScheduleCalendarUiModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus

data class ScheduleDailyUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isRefreshing: Boolean = false,
    val schedule: ScheduleCalendarUiModel? = null,
    val weeks: ScheduleWeeksUiModel? = null,
    val lessonDisplaySettings: LessonDisplaySettings = LessonDisplaySettings.Default,

    val today: LocalDate,
    val scheduleSize: Int,
    val todayScheduleIndex: Int,
    val todayWeeksIndex: Int,
    val todayDayOfWeekIndex: Int,

    val selectedDate: LocalDate = today,
    val scheduleIndex: Int = todayScheduleIndex,
    val weeksIndex: Int = todayWeeksIndex,
    val dayOfWeekIndex: Int = todayDayOfWeekIndex,

    val showBackToTodayFab: Boolean = false,
) {
    fun setSchedule(schedule: ScheduleCalendarUiModel?): ScheduleDailyUiState {
        schedule?.init(
            today = this.today,
            size = this.scheduleSize,
            todayIndex = this.todayScheduleIndex,
        )
        val weeks = schedule?.scheduleCalendar?.let { calendar ->
            ScheduleWeeksUiModel(ScheduleWeeksCalendar(calendar))
        }
        return copy(
            schedule = schedule,
            weeks = weeks,
        )
    }

    fun toDateSelected(date: LocalDate): ScheduleDailyUiState {
        return copy(
            selectedDate = date,
        ).updateIndexes()
    }

    fun toScheduleIndex(index: Int): ScheduleDailyUiState {
        val daysUntilToday = index - todayScheduleIndex
        val selectedDate = today.plus(DatePeriod(days = daysUntilToday))
        return toDateSelected(selectedDate)
    }

    private fun getScheduleIndexByDate(date: LocalDate): Int {
        return todayScheduleIndex + today.daysUntil(date)
    }

    private fun getWeeksIndex(date: LocalDate): Int {
        val daysUntilToday = today.daysUntil(date)
        val dayOfWeekIndex = getDayOfWeekIndex(date)
        val mondayIndex = todayScheduleIndex + daysUntilToday - dayOfWeekIndex
        return mondayIndex / 7
    }

    private fun getDayOfWeekIndex(date: LocalDate): Int {
        return date.dayOfWeek.isoDayNumber - 1
    }

    private fun updateIndexes(): ScheduleDailyUiState {
        val scheduleIndex = getScheduleIndexByDate(selectedDate)
        val weeksIndex = getWeeksIndex(selectedDate)
        val dayOfWeekPos = getDayOfWeekIndex(selectedDate)
        val dateIsToday = scheduleIndex == todayScheduleIndex

        return copy(
            scheduleIndex = scheduleIndex,
            weeksIndex = weeksIndex,
            dayOfWeekIndex = dayOfWeekPos,
            showBackToTodayFab = !dateIsToday,
        )
    }

    fun toLoading(isLoading: Boolean): ScheduleDailyUiState {
        return copy(
            isLoading = isLoading && !isRefreshing,
            isRefreshing = isLoading && isRefreshing,
        ).updateIsError()
    }

    private fun updateIsError(): ScheduleDailyUiState {
        val isError = !isLoading && isError
        return copy(isError = !isLoading && isError)
    }

    companion object {

        fun init(): ScheduleDailyUiState {
            val weeksCount = Int.MAX_VALUE / 7
            val daysCount = weeksCount * 7

            val today = Clock.System.nowLocalDate()

            val (todayIndex, todayWeeksIndex) = calculateTodayIndex(
                daysCount = daysCount,
                today = today,
            )

            return ScheduleDailyUiState(
                scheduleSize = daysCount,
                today = today,
                todayScheduleIndex = todayIndex,
                todayWeeksIndex = todayWeeksIndex,
                todayDayOfWeekIndex = today.dayOfWeek.isoDayNumber - 1,
            )
        }

        private fun calculateTodayIndex(
            daysCount: Int,
            today: LocalDate,
        ): Pair<Int, Int> {
            val middleIndex = daysCount / 2
            val middleIndexDayOfWeak = middleIndex % 7
            val middleMondayIndex = middleIndex - middleIndexDayOfWeak

            val todayIndexDayOfWeek = today.dayOfWeek.isoDayNumber - 1
            val todayIndex = middleMondayIndex + todayIndexDayOfWeek
            val todayWeeksIndex = todayIndexDayOfWeek / 7

            return todayIndex to todayWeeksIndex
        }
    }
}
