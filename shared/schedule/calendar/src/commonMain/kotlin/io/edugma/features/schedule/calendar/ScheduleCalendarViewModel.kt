package io.edugma.features.schedule.calendar

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.navigation.ScheduleScreens
import io.edugma.core.utils.lce.launchLce
import io.edugma.features.schedule.calendar.mapper.CalendarMapper
import io.edugma.features.schedule.calendar.usecase.GetCurrentDayIndexUseCase
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import kotlinx.datetime.LocalDate

class ScheduleCalendarViewModel(
    private val useCase: ScheduleUseCase,
    private val calendarMapper: CalendarMapper,
    private val getCurrentDayIndexUseCase: GetCurrentDayIndexUseCase,
) : BaseActionViewModel<ScheduleCalendarUiState, ScheduleCalendarAction>(
    ScheduleCalendarUiState(),
) {

    init {
        loadScheduleCalendar(isRefreshing = false)
    }

    private fun loadScheduleCalendar(isRefreshing: Boolean) {

        // TODO use isRefreshing
        launchLce(
            lceProvider = {
                useCase.getCurrentScheduleFlow()
            },
            getLceState = state::lceState,
            setLceState = { newState { copy(lceState = it) } },
            // TODO isContentEmpty false
            isContentEmpty = { false },
            isRefreshing = isRefreshing,
            onSuccess = {
                newState {
                    val schedule = calendarMapper.map(it.value)

                    val (currentWeekIndex, currentDayOfWeekIndex) = getCurrentDayIndexUseCase(
                        schedule,
                    )

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
            },
        )
    }

    override fun onAction(action: ScheduleCalendarAction) {
        when (action) {
            is ScheduleCalendarAction.OnDayClick -> onDayClick(action.date)
        }
    }

    private fun onDayClick(date: LocalDate) {
        router.navigateTo(
            ScheduleScreens.Main(date = date),
        )
    }
}
