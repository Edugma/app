package io.edugma.features.schedule.main

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.getOrDefault
import io.edugma.domain.base.utils.isFinalFailure
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDateTime
import io.edugma.domain.schedule.model.lesson.LessonInfo
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.SimpleMutator
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.schedule.model.WeekUiModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleViewModel(
    private val useCase: ScheduleUseCase
) : BaseViewModel<ScheduleState>(initState()) {

    init {
        setupMutator {
            forProp { schedule }.onChanged {
                val weeks = WeekUiModel.fromSchedule(state.schedule)
                val schedulePos = state.getSchedulePos(state.selectedDate)
                val weekPos = state.getWeeksPos(state.selectedDate)
                val dayOfWeekPos = state.getDayOfWeekPos(state.selectedDate)

                state = state.copy(
                    isPreloading = false,
                    isLoading = false,
                    weeks = weeks,
                    schedulePos = schedulePos,
                    weeksPos = weekPos,
                    dayOfWeekPos = dayOfWeekPos
                )
            }

            forProp { selectedDate }.onChanged {
                val schedulePos = state.getSchedulePos(state.selectedDate)
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
                val date = state.schedule.getOrNull(state.schedulePos)?.date ?: LocalDate.now()
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
                    mutateState {
                        val schedule = it.getOrDefault(emptyList())
                        val isLoading = it.isLoading && !state.isPreloading

                        state = state.copy(
                            schedule = schedule,
                            isLoading = isLoading
                        )
                    }
                }
            }
        }
    }

    fun onFabClick() {
        mutateState {
            state = state.copy(selectedDate = LocalDate.now())
        }
    }

    fun onSchedulePosChanged(schedulePos: Int) {
        mutateState {
            state = state.copy(schedulePos = state.coerceSchedulePos(schedulePos))
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

    fun onLessonClick(lesson: Lesson, dateTime: LessonDateTime) {
        router.navigateTo(
            ScheduleScreens.LessonInfo(
                lessonInfo = LessonInfo(
                    lesson = lesson,
                    dateTime = dateTime
                )
            )
        )
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

private fun ScheduleState.getSchedulePos(date: LocalDate): Int {
    return coerceSchedulePos(
        schedule.indexOfFirst { it.date == date }.coerceAtLeast(0)
    )
}

private fun ScheduleState.coerceSchedulePos(schedulePos: Int): Int {
    return schedulePos.coerceIn(0, (schedule.size - 1).coerceAtLeast(0))
}

data class ScheduleState(
    val isPreloading: Boolean = true,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val schedule: List<ScheduleDay> = emptyList(),
    val weeks: List<WeekUiModel> = emptyList(),

    val selectedDate: LocalDate = LocalDate.now(),
    val schedulePos: Int = 0,
    val weeksPos: Int = 0,
    val dayOfWeekPos: Int = 0,

    val showBackToTodayFab: Boolean = false
)