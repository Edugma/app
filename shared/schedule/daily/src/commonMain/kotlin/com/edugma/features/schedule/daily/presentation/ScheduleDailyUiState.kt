package com.edugma.features.schedule.daily.presentation

import com.edugma.core.api.model.LceUiState
import com.edugma.features.schedule.daily.model.ScheduleWeeksUiModel
import com.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import com.edugma.features.schedule.domain.model.schedule.CalendarSettings
import com.edugma.features.schedule.domain.model.schedule.ScheduleWeeksCalendar
import com.edugma.features.schedule.elements.model.ScheduleCalendarUiModel
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus

data class ScheduleDailyUiState(
    val lceState: LceUiState = LceUiState.init(),
    val isLoading: Boolean = false,
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
    fun toContent(schedule: ScheduleCalendarUiModel?): ScheduleDailyUiState {
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

    companion object {

        fun init(): ScheduleDailyUiState {
            val settings = CalendarSettings.Infinity

            return ScheduleDailyUiState(
                scheduleSize = settings.daysCount,
                today = settings.today,
                todayScheduleIndex = settings.todayDayIndex,
                todayWeeksIndex = settings.todayWeeksIndex,
                todayDayOfWeekIndex = settings.todayDayOfWeekIndex,
            )
        }
    }
}
