package io.edugma.features.schedule.free_place

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.place.PlaceFilters
import io.edugma.domain.schedule.model.place.PlaceInfo
import io.edugma.domain.schedule.model.place.PlaceType
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.repository.FreePlaceRepository
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class FreePlaceViewModel(
    private val repository: FreePlaceRepository,
    private val useCase: ScheduleUseCase,
) : BaseViewModelFull<FreePlaceState, FreePlaceMutator, Nothing>(
    FreePlaceState(),
    ::FreePlaceMutator,
) {
//    fun onDateRangeChange(dateRange: ClosedFloatingPointRange<Float>) {
//        mutateState {
//            setDateRange(dateRange)
//        }
//    }
//
//    fun onTimeRangeChange(dateRange: ClosedFloatingPointRange<Float>) {
//        mutateState {
//            setTimeRange(dateRange)
//        }
//    }

    init {
        viewModelScope.launch {
            useCase.getSources(ScheduleSources.Place)
                .onSuccess {
                    mutateState {
                        setPlaces(it.map { Place(it.key, it.title, PlaceType.Undefined, it.description) }.sortedBy { it.title })
                    }
                }
                .onFailure { mutateState { setPlaces(emptyList()) } }
                .collect()
        }
    }

    fun onDateSelect(date: LocalDate) {
        mutateState {
            setDate(date)
        }
    }

    fun onTimeFromSelect(timeFrom: LocalTime) {
        mutateState {
            setTimeFrom(timeFrom)
        }
    }

    fun onTimeToSelect(timeTo: LocalTime) {
        mutateState {
            setTimeTo(timeTo)
        }
    }

    fun onEnterFilterQuery(query: String) {
        mutateState {
            setFilterQuery(query)
        }
    }

    fun onFindFreePlaces() {
        viewModelScope.launch {
            repository.findFreePlaces(
                PlaceFilters(
                    ids = state.value.places.map { it.id },
                    dateTimeFrom = LocalDateTime.of(state.value.date, state.value.timeFrom),
                    dateTimeTo = LocalDateTime.of(state.value.date, state.value.timeTo),
                ),
            ).onSuccess { mutateState { setFreePlaces(it) } }
                .onFailure { mutateState { setFreePlaces(emptyMap()) } }
                .collect()
        }
    }

    fun onShowFilters() {
        mutateState {
            state = state.copy(
                showFilters = !state.showFilters,
            )
        }
    }
}

data class FreePlaceState(
//    val dateFrom: LocalDate = LocalDate.now(),
//    val dateTo: LocalDate = LocalDate.now(),
//    val datesPositionRange: ClosedFloatingPointRange<Float> = 0f..1f,
//    val timeFrom: LocalTime = LocalTime.MIN,
//    val timeTo: LocalTime = LocalTime.MAX,
//    val timesPositionRange: ClosedFloatingPointRange<Float> = 0f..1f,
    val date: LocalDate = LocalDate.now(),
    val timeFrom: LocalTime = LocalTime.MIN,
    val timeTo: LocalTime = LocalTime.MAX,
    val filterQuery: String = "",
    val places: List<Place> = emptyList(),
    val filteredPlaces: List<Place> = emptyList(),
    val freePlaces: Map<PlaceInfo, Int> = emptyMap(),
    val showFilters: Boolean = true,
) {
    companion object {
        val minPerDay = LocalTime.MAX.toSecondOfDay() / 60
        val totalDays = 100L
    }
}

// private fun initState() : FreePlaceState {
//    val dateTo = LocalDate.now().plusDays(FreePlaceState.totalDays)
//    return FreePlaceState(dateTo = dateTo)
// }

class FreePlaceMutator : BaseMutator<FreePlaceState>() {
    fun setDate(date: LocalDate) =
        set(state.date, date) {
            copy(date = it)
        }
//    fun setDateFrom(dateFrom: LocalDate) =
//        set(state.dateFrom, dateFrom) {
//            copy(dateFrom = it)
//        }
//
//    fun setDateTo(dateTo: LocalDate) =
//        set(state.dateTo, dateTo) {
//            copy(dateTo = it)
//        }
//
//    fun setDateRange(datesPositionRange: ClosedFloatingPointRange<Float>) =
//        set(state.datesPositionRange, datesPositionRange) {
//            copy(datesPositionRange = it)
//        }.then {
//            setDateFrom(LocalDate.now().plusDays((FreePlaceState.totalDays * datesPositionRange.start).roundToLong()))
//            setDateTo(LocalDate.now().plusDays((FreePlaceState.totalDays * datesPositionRange.endInclusive).roundToLong()))
//        }

    fun setTimeFrom(timeFrom: LocalTime) =
        set(state.timeFrom, timeFrom) {
            copy(timeFrom = it)
        }

    fun setTimeTo(timeTo: LocalTime) =
        set(state.timeTo, timeTo) {
            copy(timeTo = it)
        }

    fun setFilterQuery(filterQuery: String) =
        set(state.filterQuery, filterQuery) {
            copy(filterQuery = it)
        }.then {
            setFilteredPlaces(state.places.filter { it.title.contains(state.filterQuery, ignoreCase = true) })
        }

    fun setPlaces(places: List<Place>) =
        set(state.places, places) {
            copy(places = it)
        }.then {
            setFilteredPlaces(state.places.filter { it.title.contains(state.filterQuery, ignoreCase = true) })
        }

    fun setFilteredPlaces(filteredPlaces: List<Place>) =
        set(state.filteredPlaces, filteredPlaces) {
            copy(filteredPlaces = it)
        }

    fun setFreePlaces(freePlaces: Map<PlaceInfo, Int>) =
        set(state.freePlaces, freePlaces) {
            copy(freePlaces = it)
        }

//    fun setTimeRange(timesPositionRange: ClosedFloatingPointRange<Float>) =
//        set(state.timesPositionRange, timesPositionRange) {
//            copy(timesPositionRange = it)
//        }.then {
//            setTimeFrom(LocalTime.MIN.plusMinutes((FreePlaceState.minPerDay * timesPositionRange.start).roundToLong()))
//            setTimeTo(LocalTime.MIN.plusMinutes((FreePlaceState.minPerDay * timesPositionRange.endInclusive).roundToLong()))
//        }
}
