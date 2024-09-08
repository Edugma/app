package com.edugma.features.schedule.elements.verticalSchedule

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.viewmodel.BaseViewModel
import com.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.domain.model.source.ScheduleSource
import com.edugma.features.schedule.domain.usecase.ScheduleUseCase
import com.edugma.features.schedule.elements.model.ScheduleDayUiModel

class VerticalScheduleViewModel(
    private val useCase: ScheduleUseCase,
) : BaseViewModel<VerticalScheduleState>(VerticalScheduleState()) {
    init {
//        launchCoroutine {
//            stateFlow.prop { scheduleSource }.collect {
//                val lessonDisplaySettings = it?.let {
//                    useCase.getLessonDisplaySettings(it.type)
//                } ?: LessonDisplaySettings.Default
//                newState {
//                    copy(
//                        lessonDisplaySettings = lessonDisplaySettings,
//                    )
//                }
//            }
//        }
//
//        launchCoroutine {
//            stateFlow.prop { scheduleSource }.filterNotNull().collectLatest {
//                useCase.getSchedule(it).collect {
//                    if (!it.isFinalFailure) {
//                        val schedule = it.getOrDefault(emptyList())
//                        if (schedule.isEmpty() && it.isLoading) return@collect
//
//                        newState {
//                            setSchedule(
//                                schedule = schedule.toUiModel(),
//                            )
//                        }
//                    }
//                }
//            }
//        }
    }

    fun setScheduleSource(scheduleSource: ScheduleSource) {
        newState {
            copy(scheduleSource = scheduleSource)
        }
    }

    fun onLessonClick(lesson: LessonEvent) {
//        scheduleRouter.navigateTo(
//            ScheduleInfoScreens.LessonInfo(
//                lessonInfo = Json.encodeToString(
//                    lesson,
//                ),
//            ),
//        )
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
    // .updateCurrentDayIndex()

//    fun updateCurrentDayIndex(): VerticalScheduleState {
//        schedule?.let {
//            if (it.isNotEmpty()) {
//                val index = calculateTodayIndex(it)
//                return copy(
//                    currentDayIndex = index,
//                )
//            } else {
//                return this
//            }
//        }
//
//        return this
//    }

//    private fun calculateTodayIndex(scheduleDays: List<ScheduleDayUiModel>): Int {
//        var index = 0
//        val now = Clock.System.nowLocalDate()
//        scheduleDays.asSequence()
//            .map {
//                it.date to
//                    it.lessons.sumOf {
//                        when (it) {
//                            is ScheduleItem.LessonEventUiModel -> it.lesson2.lessons.size + 1
//                            is ScheduleItem.Window -> 1
//                        }
//                    }.let { if (it == 0) 1 else it }
//            }
//            .firstOrNull {
//                if (it.first == now) {
//                    true
//                } else {
//                    index += 1 + it.second
//                    false
//                }
//            }
//        return index
//    }
}
