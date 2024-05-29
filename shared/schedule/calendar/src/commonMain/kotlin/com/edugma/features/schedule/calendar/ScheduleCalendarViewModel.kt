package com.edugma.features.schedule.calendar

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import com.edugma.core.navigation.ScheduleScreens
import com.edugma.core.utils.lce.launchLce
import com.edugma.features.schedule.calendar.mapper.CalendarMapper
import com.edugma.features.schedule.domain.usecase.ScheduleUseCase
import kotlinx.datetime.LocalDate

class ScheduleCalendarViewModel(
    private val useCase: ScheduleUseCase,
    private val calendarMapper: CalendarMapper,
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
                    it.valueOrThrow.init(
                        today = settings.today,
                        size = settings.daysCount,
                        todayIndex = settings.todayDayIndex,
                    )
                    val schedule = calendarMapper.map(it.valueOrThrow)

                    if (this.schedule != schedule) {
                        copy(
                            schedule = schedule,
                            currentWeekIndex = settings.todayWeeksIndex,
                            currentDayOfWeekIndex = settings.todayDayOfWeekIndex,
                        )
                    } else {
                        this
                    }
                }
            },
        )
    }

    override fun processAction(action: ScheduleCalendarAction) {
        when (action) {
            is ScheduleCalendarAction.OnDayClick -> onDayClick(action.date)
        }
    }

    private fun onDayClick(date: LocalDate) {
        scheduleRouter.navigateTo(
            ScheduleScreens.Main(date = date),
        )
    }

    fun exit() {
        scheduleRouter.back()
    }
}
