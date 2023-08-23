package io.edugma.features.schedule.elements.verticalSchedule

import io.edugma.core.api.utils.getOrDefault
import io.edugma.core.api.utils.isFinalFailure
import io.edugma.core.api.utils.nowLocalDate
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.arch.mvi.viewmodel.prop
import io.edugma.core.navigation.schedule.ScheduleInfoScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonInfo
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import io.edugma.features.schedule.elements.utils.toUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class VerticalScheduleViewModel(
    private val useCase: ScheduleUseCase,
) : BaseViewModel<VerticalScheduleState>(VerticalScheduleState()) {
    init {
        launchCoroutine {
            stateFlow.prop { scheduleSource }.collect {
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

        launchCoroutine {
            stateFlow.prop { scheduleSource }.filterNotNull().collectLatest {
                useCase.getSchedule(it).collect {
                    if (!it.isFinalFailure) {
                        val schedule = it.getOrDefault(emptyList())
                        if (schedule.isEmpty() && it.isLoading) return@collect

                        newState {
                            setSchedule(
                                schedule = schedule.toUiModel(),
                            )
                        }
                    }
                }
            }
        }
    }

    fun setScheduleSource(scheduleSource: ScheduleSource) {
        newState {
            copy(scheduleSource = scheduleSource)
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
}

data class VerticalScheduleState(
    val scheduleSource: ScheduleSource? = null,
    val schedule: List<ScheduleDayUiModel>? = null,
    val lessonDisplaySettings: LessonDisplaySettings = LessonDisplaySettings.Default,
    val currentDayIndex: Int = 0,
) {
    fun setSchedule(schedule: List<ScheduleDayUiModel>?) =
        copy(schedule = schedule)
            .updateCurrentDayIndex()

    fun updateCurrentDayIndex(): VerticalScheduleState {
        schedule?.let {
            if (it.isNotEmpty()) {
                val index = calculateTodayIndex(it)
                return copy(
                    currentDayIndex = index,
                )
            } else {
                return this
            }
        }

        return this
    }

    private fun calculateTodayIndex(scheduleDays: List<ScheduleDayUiModel>): Int {
        var index = 0
        val now = Clock.System.nowLocalDate()
        scheduleDays.asSequence()
            .map {
                it.date to
                    it.lessons.sumOf {
                        when (it) {
                            is ScheduleItem.LessonByTime -> it.lesson.lessons.size + 1
                            is ScheduleItem.Window -> 1
                        }
                    }.let { if (it == 0) 1 else it }
            }
            .firstOrNull {
                if (it.first == now) {
                    true
                } else {
                    index += 1 + it.second
                    false
                }
            }
        return index
    }
}
