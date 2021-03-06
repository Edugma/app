package io.edugma.features.schedule.elements.vertical_schedule

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.getOrDefault
import io.edugma.domain.base.utils.isFinalFailure
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.domain.schedule.model.group.GroupInfo
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDateTime
import io.edugma.domain.schedule.model.lesson.LessonDisplaySettings
import io.edugma.domain.schedule.model.lesson.LessonInfo
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.prop
import io.edugma.features.base.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import io.edugma.features.schedule.elements.utils.toUiModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate

class VerticalScheduleViewModel(
    private val useCase: ScheduleUseCase
) : BaseViewModel<VerticalScheduleState>(VerticalScheduleState()) {
    init {
        setupMutator {
            forProp { schedule }.onChanged {
                state.schedule?.let {
                    val index = calculateTodayIndex(state.schedule!!)
                    state = state.copy(
                        currentDayIndex = index
                    )
                }
            }
        }

        viewModelScope.launch {
            useCase.getSchedule().collect {
                if (!it.isFinalFailure) {
                    val schedule = it.getOrDefault(emptyList())
                    if (schedule.isEmpty() && it.isLoading) return@collect

                    mutateState {
                        state = state.copy(
                            schedule = schedule.toUiModel()
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            state.prop { scheduleSource }.collect {
                val lessonDisplaySettings = it?.let {
                    useCase.getLessonDisplaySettings(it.type)
                } ?: LessonDisplaySettings.Default
                mutateState {
                    state = state.copy(
                        lessonDisplaySettings = lessonDisplaySettings
                    )
                }
            }
        }
    }

    private fun calculateTodayIndex(scheduleDays: List<ScheduleDayUiModel>): Int {
        var index = 0
        val now = LocalDate.now()
        scheduleDays.asSequence()
            .map { it.date to
                    it.lessons.sumOf {
                        when (it) {
                            is ScheduleItem.LessonByTime -> it.lesson.lessons.size + 1
                            is ScheduleItem.Window -> 1
                        }
                    }.let { if (it == 0) 1 else it }
            }
            .first {
                if (it.first == now) {
                    true
                } else {
                    index += 1 + it.second
                    false
                }
            }
        return index
    }

    fun setScheduleSource(scheduleSource: ScheduleSource) {
        mutateState {
            state = state.copy(scheduleSource = scheduleSource)
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
}

data class VerticalScheduleState(
    val scheduleSource: ScheduleSource? = null,
    val schedule: List<ScheduleDayUiModel>? = null,
    val lessonDisplaySettings: LessonDisplaySettings = LessonDisplaySettings.Default,
    val currentDayIndex: Int = 0
)