package io.edugma.features.schedule.scheduleInfo.groupInfo

import io.edugma.core.api.utils.onFailure
import io.edugma.core.api.utils.onSuccess
import io.edugma.core.arch.mvi.updateState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.arch.mvi.viewmodel.prop
import io.edugma.core.utils.viewmodel.launchCoroutine
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
                        updateState {
                            copy(
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
        updateState {
            copy(id = id)
        }
    }

    fun onTabSelected(tab: GroupInfoTabs) {
        updateState {
            copy(selectedTab = tab)
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
