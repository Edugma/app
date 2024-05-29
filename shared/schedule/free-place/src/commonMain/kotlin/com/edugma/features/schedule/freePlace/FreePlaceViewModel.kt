package com.edugma.features.schedule.freePlace

import com.edugma.core.api.utils.MAX
import com.edugma.core.api.utils.MIN
import com.edugma.core.api.utils.nowLocalDate
import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.BaseViewModel
import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.domain.model.place.Place
import com.edugma.features.schedule.domain.model.place.PlaceFilters
import com.edugma.features.schedule.domain.repository.FreePlaceRepository
import com.edugma.features.schedule.domain.usecase.ScheduleUseCase
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class FreePlaceViewModel(
    private val repository: FreePlaceRepository,
    private val useCase: ScheduleUseCase,
) : BaseViewModel<FreePlaceState>(FreePlaceState()) {
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

//    init {
//        launchCoroutine(
//            onError = {
//                newState {
//                    setPlaces(emptyList())
//                }
//            },
//        ) {
//            val it = useCase.getSources(ScheduleSources.Place)
//            newState {
//                setPlaces(
//                    it.map { Place(it.key, it.title, PlaceType.Undefined, it.description) }
//                        .sortedBy { it.title },
//                )
//            }
//        }
//    }

    fun onDateSelect(date: LocalDate) {
        newState {
            copy(date = date)
        }
    }

    fun onTimeFromSelect(timeFrom: LocalTime) {
        newState {
            copy(timeFrom = timeFrom)
        }
    }

    fun onTimeToSelect(timeTo: LocalTime) {
        newState {
            copy(timeTo = timeTo)
        }
    }

    fun onEnterFilterQuery(query: String) {
        newState {
            setFilterQuery(query)
        }
    }

    fun onFindFreePlaces() {
        launchCoroutine(
            onError = {
                newState {
                    copy(freePlaces = emptyMap())
                }
            },
        ) {
            repository.findFreePlaces(
                PlaceFilters(
                    ids = stateFlow.value.places.map { it.id },
                    dateTimeFrom = LocalDateTime(stateFlow.value.date, stateFlow.value.timeFrom),
                    dateTimeTo = LocalDateTime(stateFlow.value.date, stateFlow.value.timeTo),
                ),
            ).collect {
                newState {
                    copy(freePlaces = it.getOrThrow())
                }
            }
        }
    }

    fun onShowFilters() {
        newState {
            copy(
                showFilters = !showFilters,
            )
        }
    }

    fun exit() {
        scheduleRouter.back()
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
    val freePlaces: Map<CompactPlaceInfo, Int> = emptyMap(),
    val showFilters: Boolean = true,
) {
    companion object {
        val minPerDay = LocalTime.MAX.toSecondOfDay() / 60
        val totalDays = 100L
    }

    fun setFilterQuery(filterQuery: String) =
        copy(filterQuery = filterQuery)
            .setFilteredPlaces(places.filter { it.title.contains(filterQuery, ignoreCase = true) })

    fun setPlaces(places: List<Place>) =
        copy(places = places)
            .setFilteredPlaces(places.filter { it.title.contains(filterQuery, ignoreCase = true) })

    fun setFilteredPlaces(filteredPlaces: List<Place>) =
        copy(filteredPlaces = filteredPlaces)
}

// private fun initState() : FreePlaceState {
//    val dateTo = LocalDate.now().plusDays(FreePlaceState.totalDays)
//    return FreePlaceState(dateTo = dateTo)
// }
