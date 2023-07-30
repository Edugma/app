package io.edugma.features.schedule.freePlace

import io.edugma.core.api.utils.MAX
import io.edugma.core.api.utils.MIN
import io.edugma.core.api.utils.nowLocalDate
import io.edugma.core.arch.mvi.updateState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
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

    init {
        launchCoroutine(
            onError = {
                updateState {
                    setPlaces(emptyList())
                }
            },
        ) {
            val it = useCase.getSources(ScheduleSources.Place)
            updateState {
                setPlaces(
                    it.map { Place(it.key, it.title, PlaceType.Undefined, it.description) }
                        .sortedBy { it.title },
                )
            }
        }
    }

    fun onDateSelect(date: LocalDate) {
        updateState {
            copy(date = date)
        }
    }

    fun onTimeFromSelect(timeFrom: LocalTime) {
        updateState {
            copy(timeFrom = timeFrom)
        }
    }

    fun onTimeToSelect(timeTo: LocalTime) {
        updateState {
            copy(timeTo = timeTo)
        }
    }

    fun onEnterFilterQuery(query: String) {
        updateState {
            setFilterQuery(query)
        }
    }

    fun onFindFreePlaces() {
        launchCoroutine(
            onError = {
                updateState {
                    copy(freePlaces = emptyMap())
                }
            },
        ) {
            repository.findFreePlaces(
                PlaceFilters(
                    ids = state.value.places.map { it.id },
                    dateTimeFrom = LocalDateTime(state.value.date, state.value.timeFrom),
                    dateTimeTo = LocalDateTime(state.value.date, state.value.timeTo),
                ),
            ).collect {
                updateState {
                    copy(freePlaces = it.getOrThrow())
                }
            }
        }
    }

    fun onShowFilters() {
        updateState {
            copy(
                showFilters = !showFilters,
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
