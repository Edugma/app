package io.edugma.features.schedule.daily

import io.edugma.core.api.utils.getOrDefault
import io.edugma.core.api.utils.isFinalFailure
import io.edugma.core.api.utils.nowLocalDate
import io.edugma.core.arch.mvi.impl.SimpleMutator
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.arch.mvi.viewmodel.prop
import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.schedule.daily.model.WeekUiModel
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonInfo
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import io.edugma.features.schedule.elements.utils.toUiModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ScheduleViewModel(
    private val useCase: ScheduleUseCase,
) : BaseViewModel<ScheduleState>(initState()) {

    init {
        setupMutator {
            forProp { schedule }.onChanged {
                val schedule = state.schedule ?: return@onChanged

                val weeks = WeekUiModel.fromSchedule(schedule)
                val fixedSelectedDate = schedule.fixSelectedDate(state.selectedDate)
                val schedulePos = schedule.getSchedulePos(state.selectedDate)
                val weekPos = state.getWeeksPos(state.selectedDate)
                val dayOfWeekPos = state.getDayOfWeekPos(state.selectedDate)

                state = state.copy(
                    isPreloading = false,
                    weeks = weeks,
                    schedulePos = schedulePos,
                    weeksPos = weekPos,
                    dayOfWeekPos = dayOfWeekPos,
                    selectedDate = fixedSelectedDate,
                )
            }

            forProp { selectedDate }.onChanged {
                val schedule = state.schedule ?: return@onChanged
                val schedulePos = schedule.getSchedulePos(state.selectedDate)
                val weekPos = state.getWeeksPos(state.selectedDate)
                val dayOfWeekPos = state.getDayOfWeekPos(state.selectedDate)
                val dateIsToday = state.selectedDate == Clock.System.nowLocalDate()

                state = state.copy(
                    schedulePos = schedulePos,
                    weeksPos = weekPos,
                    dayOfWeekPos = dayOfWeekPos,
                    showBackToTodayFab = !dateIsToday,
                )
            }

            forProp { schedulePos }.onChanged {
                val date = state.schedule?.getOrNull(state.schedulePos)?.date ?: Clock.System.nowLocalDate()
                state = state.copy(selectedDate = date)
            }

            forProp { isLoading }.onChanged {
                val isError = !state.isLoading && state.isError
                state = state.copy(isError = isError)
            }
        }

        launchCoroutine() {
            useCase.getSchedule().collect {
                if (!it.isFinalFailure) {
                    val schedule = it.getOrDefault(emptyList())
                    if (schedule.isEmpty() && it.isLoading) return@collect
                    val isLoading = it.isLoading // && !state.isPreloading

                    mutateState {

                        state = state.copy(
                            schedule = schedule.toUiModel(),
                            isLoading = isLoading && !state.isRefreshing,
                            isRefreshing = isLoading && state.isRefreshing,
                        )
                    }
                }
            }
        }

        launchCoroutine {
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
                                isRefreshing = isLoading && state.isRefreshing,
                            )
                        }
                    }
                }
            }
        }

        launchCoroutine(
            onError = {
                mutateState {
                    state = state.copy(
                        lessonDisplaySettings = LessonDisplaySettings.Default,
                    )
                }
            },
        ) {
            useCase.getSelectedSource().collect {
                val lessonDisplaySettings = it?.let {
                    useCase.getLessonDisplaySettings(it.type)
                } ?: LessonDisplaySettings.Default
                mutateState {
                    state = state.copy(
                        lessonDisplaySettings = lessonDisplaySettings,
                    )
                }
            }
        }
    }

    fun onFabClick() {
        mutateState {
            state = state.copy(selectedDate = Clock.System.nowLocalDate())
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
                lessonInfo = Json.encodeToString(
                    LessonInfo(
                        lesson = lesson,
                        dateTime = dateTime,
                    ),
                ),
            ),
        )
    }

    fun initDate(date: LocalDate?) {
        if (date != null) {
            val schedule = state.value.schedule
            if (schedule == null) {
                launchCoroutine {
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

data class ScheduleState(
    val isPreloading: Boolean = true,
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
)
