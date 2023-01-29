package io.edugma.features.schedule.schedule_info.place_info

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.prop
import io.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import io.edugma.features.schedule.domain.model.place.PlaceInfo
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.repository.FreePlaceRepository
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PlaceInfoViewModel(
    private val repository: ScheduleInfoRepository,
    private val freePlaceRepository: FreePlaceRepository,
) : BaseViewModel<PlaceInfoState>(PlaceInfoState()) {
    init {
        viewModelScope.launch {
            state.prop { id }.filterNotNull().collect {
                repository.getPlaceInfo(it)
                    .onSuccess {
                        mutateState {
                            state = state.copy(
                                placeInfo = it,
                                scheduleSource = ScheduleSource(
                                    type = ScheduleSources.Place,
                                    key = it.id,
                                ),
                            )
                        }
                    }.onFailure {
                    }.collect()
            }
        }

        viewModelScope.launch {
            state.prop { id }.filterNotNull().collect {
                freePlaceRepository.getPlaceOccupancy(it)
                    .onSuccess {
                        mutateState {
                            state = state.copy(placeOccupancy = it)
                        }
                    }.onFailure {
                    }.collect()
            }
        }
    }

    fun onTabSelected(tab: PlaceInfoTabs) {
        mutateState {
            state = state.copy(selectedTab = tab)
        }
    }

    fun setId(id: String) {
        mutateState {
            state = state.copy(
                id = id,
            )
        }
    }
}

data class PlaceInfoState(
    val id: String? = null,
    val placeInfo: PlaceInfo? = null,
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
