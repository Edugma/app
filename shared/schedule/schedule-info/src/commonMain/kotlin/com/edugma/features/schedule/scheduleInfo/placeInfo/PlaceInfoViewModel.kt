package com.edugma.features.schedule.scheduleInfo.placeInfo

import com.edugma.core.api.utils.onFailure
import com.edugma.core.api.utils.onSuccess
import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic2
import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import com.edugma.features.schedule.domain.model.source.ScheduleSource
import com.edugma.features.schedule.domain.repository.FreePlaceRepository
import com.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull

class PlaceInfoViewModel(
    private val repository: ScheduleInfoRepository,
    private val freePlaceRepository: FreePlaceRepository,
) : FeatureLogic2<PlaceInfoState>() {
    override fun initialState(): PlaceInfoState {
        return PlaceInfoState()
    }

    override fun onCreate() {
        // TODO
//        launchCoroutine {
//            stateFlow.prop { id }.filterNotNull().collect {
//                repository.getPlaceInfo(it)
//                    .onSuccess {
//                        newState {
//                            copy(
//                                placeInfo = it,
//                                scheduleSource = ScheduleSource(
//                                    type = "place",
//                                    key = it.id,
//                                ),
//                            )
//                        }
//                    }.onFailure {
//                    }.collect()
//            }
//        }

        launchCoroutine {
            stateFlow.mapNotNull { it.id }.collect {
                freePlaceRepository.getPlaceOccupancy(it)
                    .onSuccess {
                        newState {
                            copy(placeOccupancy = it)
                        }
                    }.onFailure {
                    }.collect()
            }
        }
    }

    fun onTabSelected(tab: PlaceInfoTabs) {
        newState {
            copy(selectedTab = tab)
        }
    }

    fun setId(id: String) {
        newState {
            copy(
                id = id,
            )
        }
    }

    fun exit() {
        scheduleRouter.back()
    }
}

data class PlaceInfoState(
    val id: String? = null,
    val placeInfo: CompactPlaceInfo? = null,
    val tabs: List<PlaceInfoTabs> = listOf(PlaceInfoTabs.Schedule),
    val selectedTab: PlaceInfoTabs = PlaceInfoTabs.Schedule,
    val placeOccupancy: List<PlaceDailyOccupancy> = emptyList(),
    val scheduleSource: ScheduleSource? = null,
)

enum class PlaceInfoTabs {
    Occupancy,
    Map,
    Schedule,
}
