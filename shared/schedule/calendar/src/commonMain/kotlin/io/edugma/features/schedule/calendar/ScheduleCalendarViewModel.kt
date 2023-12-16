package io.edugma.features.schedule.calendar

import io.edugma.core.api.utils.getOrDefault
import io.edugma.core.api.utils.isFinalFailure
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.ScheduleScreens
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
            useCase.getCurrentScheduleFlow().collect {
                if (!it.isFinalFailure) {
                    newState {
                        val schedule = calendarMapper.map(it.getOrDefault(emptyList()))

                        val (currentWeekIndex, currentDayOfWeekIndex) = getCurrentDayIndex(schedule)

                        if (this.schedule != schedule) {
                            copy(
                                schedule = schedule,
                                currentWeekIndex = currentWeekIndex,
                                currentDayOfWeekIndex = currentDayOfWeekIndex,
                            )
                        } else {
                            this
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
