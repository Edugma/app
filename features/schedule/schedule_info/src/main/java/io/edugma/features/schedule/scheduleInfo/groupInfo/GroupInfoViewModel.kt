package io.edugma.features.schedule.scheduleInfo.groupInfo

import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.prop
import io.edugma.features.schedule.domain.model.group.GroupInfo
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

class GroupInfoViewModel(
    private val repository: ScheduleInfoRepository,
) : BaseViewModel<GroupInfoState>(GroupInfoState()) {
    init {
        launchCoroutine {
            state.prop { id }.filterNotNull().collect {
                repository.getGroupInfo(it)
                    .onSuccess {
                        mutateState {
                            state = state.copy(
                                groupInfo = it,
                                scheduleSource = ScheduleSource(
                                    type = ScheduleSources.Group,
                                    key = it.id,
                                ),
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
    val scheduleSource: ScheduleSource? = null,
)

enum class GroupInfoTabs {
    Schedule,
    Students,
}
