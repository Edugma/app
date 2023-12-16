package io.edugma.features.schedule.daily.presentation

import io.edugma.core.api.utils.nowLocalDate
import io.edugma.core.api.utils.onResult
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import io.edugma.features.schedule.elements.utils.toUiModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ScheduleViewModel(
    private val useCase: ScheduleUseCase,
) : BaseActionViewModel<ScheduleDailyUiState, ScheduleDailyAction>(
    ScheduleDailyUiState.init(),
) {

    init {
        launchCoroutine() {
            getSchedule(true)
        }

        launchCoroutine(
            onError = {
                newState {
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
                newState {
                    copy(
                        lessonDisplaySettings = lessonDisplaySettings,
                    )
                }
            }
        }
    }

    private suspend fun getSchedule(forceUpdate: Boolean = false) {
        useCase.getCurrentScheduleFlow(forceUpdate = forceUpdate).onResult(
            onSuccess = {
                newState {
                    setSchedule(it.value.toUiModel())
                        .toLoading(it.isLoading)
                }
            },
            onFailure = {
                if (it.isLoading) {
                    newState {
                        toLoading(it.isLoading)
                    }
                } else {
                    // TODO on failure
                }
            },
        )
    }

    override fun onAction(action: ScheduleDailyAction) {
        when (action) {
            ScheduleDailyAction.OnBack -> router.back()
            ScheduleDailyAction.OnFabClick -> newState {
                toDateSelected(date = Clock.System.nowLocalDate())
            }
            ScheduleDailyAction.OnRefreshing -> onRefreshing()
            is ScheduleDailyAction.OnDayClick -> newState {
                toDateSelected(date = action.date)
            }
            is ScheduleDailyAction.OnLessonClick -> onLessonClick(
                lesson = action.lesson,
            )
            is ScheduleDailyAction.OnWeeksPosChanged -> newState {
                copy(weeksIndex = action.weeksPos)
            }
            is ScheduleDailyAction.OnSchedulePosChanged -> newState {
                toScheduleIndex(action.schedulePos)
            }
        }
    }

    private fun onRefreshing() {
        newState {
            copy(isRefreshing = !isLoading)
        }
        launchCoroutine {
            getSchedule(forceUpdate = true)
        }
    }

    private fun onLessonClick(lesson: LessonEvent) {
        val date = state.selectedDate
        router.navigateTo(
            ScheduleInfoScreens.LessonInfo(
                lessonInfo = Json.encodeToString(
                    lesson,
                ),
            ),
        )
    }

    fun onArgs(date: LocalDate?) {
        if (date != null) {
            newState {
                toDateSelected(date)
            }
        }
    }

    private fun fixAndSetDate(date: LocalDate, schedule: List<ScheduleDayUiModel>) {
        newState {
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
