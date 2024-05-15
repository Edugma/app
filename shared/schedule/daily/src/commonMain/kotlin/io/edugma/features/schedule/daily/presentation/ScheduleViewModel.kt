package io.edugma.features.schedule.daily.presentation

import io.edugma.core.api.utils.nowLocalDate
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.core.utils.lce.launchLce
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
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
        loadSchedule(isRefreshing = false)

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

    private fun loadSchedule(isRefreshing: Boolean) {
        launchLce(
            lceProvider = {
                useCase.getCurrentScheduleFlow(forceUpdate = isRefreshing)
            },
            getLceState = state::lceState,
            setLceState = { newState { copy(lceState = it) } },
            // TODO isContentEmpty
            isContentEmpty = { false },
            isRefreshing = isRefreshing,
            onSuccess = {
                newState {
                    toContent(it.valueOrThrow.toUiModel())
                }
            },
        )
    }

    override fun onAction(action: ScheduleDailyAction) {
        when (action) {
            ScheduleDailyAction.OnBack -> exit()
            ScheduleDailyAction.OnFabClick -> newState {
                toDateSelected(date = Clock.System.nowLocalDate())
            }
            ScheduleDailyAction.OnRefresh -> onRefreshing()
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
        loadSchedule(isRefreshing = true)
    }

    private fun onLessonClick(lesson: LessonEvent) {
        val date = state.selectedDate
        scheduleRouter.navigateTo(
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

    fun exit() {
        scheduleRouter.back()
    }
}
