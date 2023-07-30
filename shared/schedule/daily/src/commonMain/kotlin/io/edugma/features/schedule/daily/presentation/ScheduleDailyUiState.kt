package io.edugma.features.schedule.daily.presentation

import io.edugma.core.api.utils.nowLocalDate
import io.edugma.features.schedule.daily.model.WeekUiModel
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

data class ScheduleDailyUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isRefreshing: Boolean = false,
    val schedule: List<ScheduleDayUiModel>? = null,
    val weeks: List<WeekUiModel> = emptyList(),
    val lessonDisplaySettings: LessonDisplaySettings = LessonDisplaySettings.Default,

    val selectedDate: LocalDate = Clock.System.nowLocalDate(),
    val schedulePos: Int = 0,
    val weeksPos: Int = 0,
    val dayOfWeekPos: Int = 0,

    val showBackToTodayFab: Boolean = false,
) {
    fun setSchedule(schedule: List<ScheduleDayUiModel>?): ScheduleDailyUiState {
        return copy(
            schedule = schedule,
        ).setDates()
    }

    fun setIsLoading(isLoading: Boolean): ScheduleDailyUiState {
        return copy(
            isLoading = isLoading && !isRefreshing,
            isRefreshing = isLoading && isRefreshing,
        ).updateIsError()
    }
    fun updateIsError(): ScheduleDailyUiState {
        val isError = !isLoading && isError
        return copy(isError = !isLoading && isError)
    }

    fun setDates(): ScheduleDailyUiState {
        val schedule = schedule ?: return this

        val weeks = WeekUiModel.fromSchedule(schedule)
        val fixedSelectedDate = schedule.fixSelectedDate(selectedDate)
        val schedulePos = schedule.getSchedulePos(selectedDate)
        val weekPos = getWeeksPos(selectedDate)
        val dayOfWeekPos = getDayOfWeekPos(selectedDate)

        return copy(
            weeks = weeks,
            schedulePos = schedulePos,
            weeksPos = weekPos,
            dayOfWeekPos = dayOfWeekPos,
            selectedDate = fixedSelectedDate,
        )
    }

    fun updateDatePos(): ScheduleDailyUiState {
        val date = schedule?.getOrNull(schedulePos)?.date ?: Clock.System.nowLocalDate()
        return copy(selectedDate = date).updatePositions()
    }

    fun setSelectedDate(selectedDate: LocalDate) =
        copy(selectedDate = selectedDate)
            .updatePositions()

    fun updatePositions(): ScheduleDailyUiState {
        val schedule = schedule ?: return this
        val schedulePos = schedule.getSchedulePos(selectedDate)
        val weekPos = getWeeksPos(selectedDate)
        val dayOfWeekPos = getDayOfWeekPos(selectedDate)
        val dateIsToday = selectedDate == Clock.System.nowLocalDate()

        return copy(
            schedulePos = schedulePos,
            weeksPos = weekPos,
            dayOfWeekPos = dayOfWeekPos,
            showBackToTodayFab = !dateIsToday,
        )
    }

    fun setSchedulePos(schedulePos: Int): ScheduleDailyUiState {
        schedule ?: return this
        return copy(
            schedulePos = schedule.coerceSchedulePos(schedulePos),
        ).updateDatePos()
    }

    fun getWeeksPos(date: LocalDate): Int {
        return weeks.indexOfFirst { it.days.any { it.date == date } }.coerceAtLeast(0)
    }

    fun getDayOfWeekPos(date: LocalDate): Int {
        return date.dayOfWeek.isoDayNumber - 1
    }

    private fun List<ScheduleDayUiModel>.fixSelectedDate(date: LocalDate): LocalDate {
        val schedulePos = getSchedulePos(date)
        return this[schedulePos].date
    }

    private fun List<ScheduleDayUiModel>.getSchedulePos(date: LocalDate): Int {
        var index = this.indexOfFirst { it.date == date }
        if (index == -1) {
            index = if (this.all { it.date < date }) {
                this.lastIndex
            } else {
                0
            }
        }
        return coerceSchedulePos(index)
    }

    private fun List<ScheduleDayUiModel>.coerceSchedulePos(schedulePos: Int): Int {
        return schedulePos.coerceIn(0, (size - 1).coerceAtLeast(0))
    }
}
