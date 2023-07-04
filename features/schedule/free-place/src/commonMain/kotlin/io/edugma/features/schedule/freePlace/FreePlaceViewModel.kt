package io.edugma.features.schedule.freePlace

import io.edugma.core.api.utils.MAX
import io.edugma.core.api.utils.MIN
import io.edugma.core.api.utils.nowLocalDate
import io.edugma.core.arch.mvi.BaseMutator
import io.edugma.core.arch.mvi.viewmodel.BaseViewModelFull
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.place.PlaceFilters
import io.edugma.features.schedule.domain.model.place.PlaceInfo
import io.edugma.features.schedule.domain.model.place.PlaceType
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.repository.FreePlaceRepository
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

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
        launchCoroutine(
            onError = {
                mutateState { setPlaces(emptyList()) }
            },
        ) {
            val it = useCase.getSources(ScheduleSources.Place)
            mutateState {
                setPlaces(it.map { Place(it.key, it.title, PlaceType.Undefined, it.description) }.sortedBy { it.title })
            }
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
        launchCoroutine(
            onError = {
                mutateState { setFreePlaces(emptyMap()) }
            },
        ) {
            repository.findFreePlaces(
                PlaceFilters(
                    ids = state.value.places.map { it.id },
                    dateTimeFrom = LocalDateTime(state.value.date, state.value.timeFrom),
                    dateTimeTo = LocalDateTime(state.value.date, state.value.timeTo),
                ),
            ).collect { mutateState { setFreePlaces(it.getOrThrow()) } }
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
    val date: LocalDate = Clock.System.nowLocalDate(),
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
