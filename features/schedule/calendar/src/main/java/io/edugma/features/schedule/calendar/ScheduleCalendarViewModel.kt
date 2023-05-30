package io.edugma.features.schedule.calendar

import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.domain.base.utils.getOrDefault
import io.edugma.domain.base.utils.isFinalFailure
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.schedule.calendar.mapper.CalendarMapper
import io.edugma.features.schedule.calendar.model.CalendarScheduleVO
import io.edugma.features.schedule.calendar.usecase.GetCurrentDayIndex
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import kotlinx.datetime.LocalDate

class ScheduleCalendarViewModel(
    private val useCase: ScheduleUseCase,
    private val calendarMapper: CalendarMapper,
    private val getCurrentDayIndex: GetCurrentDayIndex,
) : BaseViewModel<ScheduleCalendarState>(ScheduleCalendarState()) {
    init {
        launchCoroutine {
            useCase.getSchedule().collect {
                if (!it.isFinalFailure) {
                    mutateState {
                        val schedule = calendarMapper.map(it.getOrDefault(emptyList()))

                        val (currentWeekIndex, currentDayOfWeekIndex) = getCurrentDayIndex(schedule)

                        if (state.schedule != schedule) {
                            state = state.copy(
                                schedule = schedule,
                                currentWeekIndex = currentWeekIndex,
                                currentDayOfWeekIndex = currentDayOfWeekIndex,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onDayClick(date: LocalDate) {
        router.navigateTo(
            ScheduleScreens.Main(date = date),
        )
    }
}

data class ScheduleCalendarState(
    val schedule: List<CalendarScheduleVO> = emptyList(),
    val currentWeekIndex: Int = -1,
    val currentDayOfWeekIndex: Int = -1,
)
