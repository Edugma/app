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
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.source.ScheduleSources
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
    private val repository: ScheduleInfoRepository
) : BaseViewModel<GroupInfoState>(GroupInfoState()) {
    init {
        viewModelScope.launch {
            state.prop { id }.filterNotNull().collect {
                repository.getGroupInfo(it)
                    .onSuccess {
                        mutateState {
                            state = state.copy(
                                groupInfo = it,
                                scheduleSource = ScheduleSource(
                                    type = ScheduleSources.Group,
                                    key = it.id
                                )
                            )
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
}

data class GroupInfoState(
    val id: String? = null,
    val groupInfo: GroupInfo? = null,
    val tabs: List<GroupInfoTabs> = listOf(GroupInfoTabs.Schedule),
    val selectedTab: GroupInfoTabs = GroupInfoTabs.Schedule,
    val scheduleSource: ScheduleSource? = null
)

enum class GroupInfoTabs {
    Schedule,
    Students
}