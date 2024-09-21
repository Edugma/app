package com.edugma.features.schedule.scheduleInfo.groupInfo

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic2
import com.edugma.features.schedule.domain.model.group.GroupInfo
import com.edugma.features.schedule.domain.model.source.ScheduleSource
import com.edugma.features.schedule.domain.repository.ScheduleInfoRepository

class GroupInfoViewModel(
    private val repository: ScheduleInfoRepository,
) : FeatureLogic2<GroupInfoState>() {
    override fun initialState(): GroupInfoState {
        return GroupInfoState()
    }
    init {
//        launchCoroutine {
//            stateFlow.prop { id }.filterNotNull().collect {
//                repository.getGroupInfo(it)
//                    .onSuccess {
//                        newState {
//                            copy(
//                                groupInfo = it,
//                                scheduleSource = ScheduleSource(
//                                    type = "group",
//                                    key = it.id,
//                                ),
//                            )
//                        }
//                    }.onFailure {
//                    }.collect()
//            }
//        }
    }

    fun setId(id: String) {
        newState {
            copy(id = id)
        }
    }

    fun onTabSelected(tab: GroupInfoTabs) {
        newState {
            copy(selectedTab = tab)
        }
    }

    fun exit() {
        scheduleRouter.back()
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
