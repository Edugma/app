package io.edugma.features.schedule.daily.presentation

import io.edugma.core.api.utils.getOrThrow
import io.edugma.core.api.utils.nowLocalDate
import io.edugma.core.arch.mvi.updateState
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.arch.mvi.viewmodel.prop
import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonInfo
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import io.edugma.features.schedule.elements.utils.toUiModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ScheduleViewModel(
    private val useCase: ScheduleUseCase,
) : BaseActionViewModel<ScheduleDailyUiState, ScheduleDailyAction>(initState()) {

    init {
        launchCoroutine() {
            getSchedule(true)
        }

        launchCoroutine(
            onError = {
                updateState {
                    copy(
                        lessonDisplaySettings = LessonDisplaySettings.Default,
                    )
                }
            },
        ) {
            useCase.getSelectedSource().collect {
                val lessonDisplaySettings = it?.let {
                    useCase.getLessonDisplaySettings(it.type)
                } ?: LessonDisplaySettings.Default
                updateState {
                    copy(
                        lessonDisplaySettings = lessonDisplaySettings,
                    )
                }
            }
        }
    }

    private suspend fun getSchedule(forceUpdate: Boolean = false) {
        useCase.getSchedule(forceUpdate = forceUpdate).collect {
            if (it.isLoading) {
                if (it.isSuccess) {
                    val schedule = it.getOrThrow()
                    updateState {
                        setSchedule(schedule.toUiModel())
                            .setIsLoading(it.isLoading)
                    }
                } else {
                    updateState {
                        setIsLoading(it.isLoading)
                    }
                }
            } else if (it.isSuccess) {
                val schedule = it.getOrThrow()
                updateState {
                    setSchedule(schedule.toUiModel())
                        .setIsLoading(it.isLoading)
                }
            } else {
                // TODO on failure
            }
        }
    }

    override fun onAction(action: ScheduleDailyAction) {
        when (action) {
            ScheduleDailyAction.OnBack -> router.back()
            ScheduleDailyAction.OnFabClick -> updateState {
                setSelectedDate(selectedDate = Clock.System.nowLocalDate())
            }
            ScheduleDailyAction.OnRefreshing -> onRefreshing()
            is ScheduleDailyAction.OnDayClick -> updateState {
                setSelectedDate(selectedDate = action.date)
            }
            is ScheduleDailyAction.OnLessonClick -> onLessonClick(
                lesson = action.lesson,
                dateTime = action.dateTime,
            )
            is ScheduleDailyAction.OnWeeksPosChanged -> updateState {
                copy(weeksPos = action.weeksPos)
            }
            is ScheduleDailyAction.OnSchedulePosChanged -> updateState {
                setSchedulePos(action.schedulePos)
            }
        }
    }

    private fun onRefreshing() {
        updateState {
            copy(isRefreshing = !isLoading)
        }
        launchCoroutine {
            getSchedule(forceUpdate = true)
        }
    }

    private fun onLessonClick(lesson: Lesson, dateTime: LessonDateTime) {
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
        updateState {
            var fixedDate = date
            val firstDate = schedule.first().date
            val lastDate = schedule.last().date
            if (date < firstDate) {
                fixedDate = firstDate
            } else if (date > lastDate) {
                fixedDate = lastDate
            }
            copy(selectedDate = fixedDate)
        }
    }
}

private fun initState(): ScheduleDailyUiState {
    var state = ScheduleDailyUiState()
    val pos = state.getWeeksPos(state.selectedDate)
    state = state.copy(weeksPos = pos)
    val weakPos = state.getDayOfWeekPos(state.selectedDate)
    state = state.copy(dayOfWeekPos = weakPos)
    return state
}
