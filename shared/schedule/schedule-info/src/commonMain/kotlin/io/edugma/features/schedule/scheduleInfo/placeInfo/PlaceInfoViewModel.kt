package io.edugma.features.schedule.scheduleInfo.placeInfo

import io.edugma.core.api.utils.onFailure
import io.edugma.core.api.utils.onSuccess
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.arch.mvi.viewmodel.prop
import io.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import io.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.repository.FreePlaceRepository
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

class PlaceInfoViewModel(
    private val repository: ScheduleInfoRepository,
    private val freePlaceRepository: FreePlaceRepository,
) : BaseViewModel<PlaceInfoState>(PlaceInfoState()) {
    init {
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
            stateFlow.prop { id }.filterNotNull().collect {
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
