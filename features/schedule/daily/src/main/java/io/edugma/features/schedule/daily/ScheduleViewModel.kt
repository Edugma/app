package io.edugma.features.schedule.daily

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.getOrDefault
import io.edugma.domain.base.utils.isFinalFailure
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDateTime
import io.edugma.domain.schedule.model.lesson.LessonDisplaySettings
import io.edugma.domain.schedule.model.lesson.LessonInfo
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.impl.SimpleMutator
import io.edugma.features.base.core.mvi.prop
import io.edugma.features.base.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.schedule.daily.model.WeekUiModel
import io.edugma.features.schedule.elements.utils.toUiModel
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleViewModel(
    private val useCase: ScheduleUseCase
) : BaseViewModel<ScheduleState>(initState()) {

    init {
        setupMutator {
            forProp { schedule }.onChanged {
                val schedule = state.schedule ?: return@onChanged

                val weeks = WeekUiModel.fromSchedule(schedule)
                val schedulePos = schedule.getSchedulePos(state.selectedDate)
                val weekPos = state.getWeeksPos(state.selectedDate)
                val dayOfWeekPos = state.getDayOfWeekPos(state.selectedDate)

                state = state.copy(
                    isPreloading = false,
                    weeks = weeks,
                    schedulePos = schedulePos,
                    weeksPos = weekPos,
                    dayOfWeekPos = dayOfWeekPos
                )
            }

            forProp { selectedDate }.onChanged {
                val schedule = state.schedule ?: return@onChanged
                val schedulePos = schedule.getSchedulePos(state.selectedDate)
                val weekPos = state.getWeeksPos(state.selectedDate)
                val dayOfWeekPos = state.getDayOfWeekPos(state.selectedDate)
                val dateIsToday = state.selectedDate == LocalDate.now()

                state = state.copy(
                    schedulePos = schedulePos,
                    weeksPos = weekPos,
                    dayOfWeekPos = dayOfWeekPos,
                    showBackToTodayFab = !dateIsToday
                )
            }

            forProp { schedulePos }.onChanged {
                val date = state.schedule?.getOrNull(state.schedulePos)?.date ?: LocalDate.now()
                state = state.copy(selectedDate = date)
            }

            forProp { isLoading }.onChanged {
                val isError = !state.isLoading && state.isError
                state = state.copy(isError = isError)
            }
        }


        viewModelScope.launch {
            useCase.getSchedule().collect {
                if (!it.isFinalFailure) {
                    val schedule = it.getOrDefault(emptyList())
                    if (schedule.isEmpty() && it.isLoading) return@collect
                    val isLoading = it.isLoading// && !state.isPreloading

                    mutateState {

                        state = state.copy(
                            schedule = schedule.toUiModel(),
                            isLoading = isLoading && !state.isRefreshing,
                            isRefreshing = isLoading && state.isRefreshing
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            state.prop { isRefreshing }.filter { it }.collect {
                useCase.getSchedule(forceUpdate = true).collect {
                    if (!it.isFinalFailure) {
                        val schedule = it.getOrDefault(emptyList())
                        if (schedule.isEmpty() && it.isLoading) return@collect
                        val isLoading = it.isLoading

                        mutateState {

                            state = state.copy(
                                schedule = schedule.toUiModel(),
                                isLoading = isLoading && !state.isRefreshing,
                                isRefreshing = isLoading && state.isRefreshing
                            )
                        }
                    }
                }
            }
        }

        viewModelScope.launch {
            useCase.getSelectedSource()
                .onSuccess {
                    val lessonDisplaySettings = it?.let {
                        useCase.getLessonDisplaySettings(it.type)
                    } ?: LessonDisplaySettings.Default
                    mutateState {
                        state = state.copy(
                            lessonDisplaySettings = lessonDisplaySettings
                        )
                    }
                }.onFailure {
                    mutateState {
                        state = state.copy(
                            lessonDisplaySettings = LessonDisplaySettings.Default
                        )
                    }
                }.collect()
        }
    }

    fun onFabClick() {
        mutateState {
            state = state.copy(selectedDate = LocalDate.now())
        }
    }

    fun onSchedulePosChanged(schedulePos: Int) {
        mutateState {
            state.schedule?.apply {
                state = state.copy(schedulePos = coerceSchedulePos(schedulePos))
            }
        }
    }

    fun onWeeksPosChanged(weeksPos: Int) {
        mutateState {
            state = state.copy(weeksPos = weeksPos)
        }
    }

    fun onDayClick(date: LocalDate) {
        mutateState {
            state = state.copy(selectedDate = date)
        }
    }

    fun onRefreshing() {
        mutateState {
            state =
                state.copy(isRefreshing = !state.isLoading && !state.isPreloading)
        }
    }

    fun onLessonClick(lesson: Lesson, dateTime: LessonDateTime) {
        router.navigateTo(
            ScheduleInfoScreens.LessonInfo(
                lessonInfo = LessonInfo(
                    lesson = lesson,
                    dateTime = dateTime
                )
            )
        )
    }

    fun initDate(date: LocalDate?) {
        if (date != null) {
            val schedule = state.value.schedule
            if (schedule == null) {
                viewModelScope.launch {
                    val newSchedule = state.prop { this.schedule }
                        .filterNotNull()
                        .first()
                    fixAndSetDate(date, newSchedule)
                }
            } else {
                fixAndSetDate(date, schedule)
            }
        }
    }

    private fun fixAndSetDate(date: LocalDate, schedule: List<ScheduleDayUiModel>) {
        mutateState {
            var fixedDate = date
            val firstDate = schedule.first().date
            val lastDate = schedule.last().date
            if (date < firstDate) {
                fixedDate = firstDate
            } else if (date > lastDate) {
                fixedDate = lastDate
            }
            state = state.copy(selectedDate = fixedDate)
        }
    }
}

private fun initState(): ScheduleState {
    return SimpleMutator<ScheduleState>().apply {
        this.state = ScheduleState()
        mutationScope {
            val pos = state.getWeeksPos(state.selectedDate)
            state = state.copy(weeksPos = pos)
            state = state.copy(dayOfWeekPos = state.getDayOfWeekPos(state.selectedDate))
        }
    }.state
}

private fun ScheduleState.getWeeksPos(date: LocalDate): Int {
    return weeks.indexOfFirst { it.days.any { it.date == date } }.coerceAtLeast(0)
}

private fun ScheduleState.getDayOfWeekPos(date: LocalDate): Int {
    return date.dayOfWeek.value - 1
}

private fun List<ScheduleDayUiModel>.getSchedulePos(date: LocalDate): Int {
    return coerceSchedulePos(
        this.indexOfFirst { it.date == date }.coerceAtLeast(0)
    )
}

private fun List<ScheduleDayUiModel>.coerceSchedulePos(schedulePos: Int): Int {
    return schedulePos.coerceIn(0, (size - 1).coerceAtLeast(0))
}

data class ScheduleState(
    val isPreloading: Boolean = true,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isRefreshing: Boolean = false,
    val schedule: List<ScheduleDayUiModel>? = null,
    val weeks: List<WeekUiModel> = emptyList(),
    val lessonDisplaySettings: LessonDisplaySettings = LessonDisplaySettings.Default,

    val selectedDate: LocalDate = LocalDate.now(),
    val schedulePos: Int = 0,
    val weeksPos: Int = 0,
    val dayOfWeekPos: Int = 0,

    val showBackToTodayFab: Boolean = false
)