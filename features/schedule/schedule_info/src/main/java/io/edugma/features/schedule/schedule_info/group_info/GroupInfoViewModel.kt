package io.edugma.features.schedule.schedule_info.group_info

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
import io.edugma.domain.schedule.repository.ScheduleInfoRepository
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.prop
import io.edugma.features.base.navigation.schedule.ScheduleInfoScreens
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import io.edugma.features.schedule.elements.utils.toUiModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate

class GroupInfoViewModel(
    private val repository: ScheduleInfoRepository,
    private val useCase: ScheduleUseCase
) : BaseViewModel<GroupInfoState>(GroupInfoState()) {
    init {
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

        viewModelScope.launch {
            state.prop { id }.filterNotNull().collect {
                repository.getGroupInfo(it)
                    .onSuccess {
                        mutateState {
                            state = state.copy(groupInfo = it)
                        }
                    }.onFailure {

                    }.collect()
            }
        }
    }

    fun setId(id: String) {
        mutateState {
            state = state.copy(id = id)
        }
    }

    fun onTabSelected(tab: GroupInfoTabs) {
        mutateState {
            state = state.copy(selectedTab = tab)
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

data class GroupInfoState(
    val id: String? = null,
    val groupInfo: GroupInfo? = null,
    val tabs: List<GroupInfoTabs> = GroupInfoTabs.values().toList(),
    val selectedTab: GroupInfoTabs = GroupInfoTabs.Schedule,
    val schedule: List<ScheduleDayUiModel>? = null,
    val lessonDisplaySettings: LessonDisplaySettings = LessonDisplaySettings.Default,
)

enum class GroupInfoTabs {
    Schedule,
    Students
}