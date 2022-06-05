package io.edugma.features.schedule.schedule_info.place_info

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.domain.schedule.model.place.PlaceDailyOccupancy
import io.edugma.domain.schedule.model.place.PlaceInfo
import io.edugma.domain.schedule.repository.FreePlaceRepository
import io.edugma.domain.schedule.repository.ScheduleInfoRepository
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.prop
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PlaceInfoViewModel(
    private val repository: ScheduleInfoRepository,
    private val freePlaceRepository: FreePlaceRepository
) : BaseViewModel<PlaceInfoState>(PlaceInfoState()) {
    init {
        viewModelScope.launch {
            state.prop { id }.filterNotNull().collect {
                repository.getPlaceInfo(it)
                    .onSuccess {
                        mutateState {
                            state = state.copy(placeInfo = it)
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
                id = id
            )
        }
    }
}

data class PlaceInfoState(
    val id: String? = null,
    val placeInfo: PlaceInfo? = null,
    val tabs: List<PlaceInfoTabs> = PlaceInfoTabs.values().toList(),
    val selectedTab: PlaceInfoTabs = PlaceInfoTabs.Occupancy,
    val placeOccupancy: List<PlaceDailyOccupancy> = emptyList(),
)

enum class PlaceInfoTabs {
    Occupancy,
    Map,
    Schedule
}