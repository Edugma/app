package com.edugma.features.account.performance

import co.touchlab.kermit.Logger
import com.edugma.core.api.model.ListItemUiModel
import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import com.edugma.core.utils.lce.launchLce
import com.edugma.features.account.domain.model.performance.GradePosition
import com.edugma.features.account.domain.model.performance.PerformancePeriod
import com.edugma.features.account.domain.repository.PerformanceRepository
import com.edugma.features.account.performance.Filter.Name
import com.edugma.features.account.performance.Filter.PerformancePeriodUiModel
import com.edugma.features.account.performance.Filter.Type

class PerformanceViewModel(
    private val repository: PerformanceRepository,
) : BaseActionViewModel<PerformanceUiState, PerformanceAction>(PerformanceUiState()) {

    init {
        loadMarks(isRefreshing = false)
    }

    private fun loadMarks(isRefreshing: Boolean) {
        launchLce(
            lceProvider = {
                repository.getPerformance(
                    periodId = state.selectedPeriod?.id,
                    forceUpdate = isRefreshing,
                )
            },
            getLceState = state::lceState,
            setLceState = {
                newState { copy(lceState = it) }
            },
            isContentEmpty = { state.performanceList == null },
            isRefreshing = isRefreshing,
            onSuccess = {
                newState {
                    toContent(it.valueOrThrow)
                }
            },
        )
    }

    override fun processAction(action: PerformanceAction) {
        when (action) {
            is PerformanceAction.OnPeriodSelected -> onPeriodSelected(action.period)
            PerformanceAction.OnRefresh -> loadMarks(isRefreshing = true)
            is PerformanceAction.OnPerformanceClicked -> onPerformanceClicked(action.gradePosition)
        }
    }

    private fun onPerformanceClicked(gradePosition: GradePosition?) {
        newState {
            copy(selectedPerformance = gradePosition)
        }
    }

    private fun onPeriodSelected(period: PerformancePeriodUiModel) {
        if (state.selectedPeriod?.id != period.id) {
            newState {
                toPeriodSelected(period)
            }
            loadMarks(isRefreshing = false)
        }
    }

    fun openBottomSheetClick(performance: GradePosition?) {
        newState {
            copy(selectedPerformance = performance)
        }
    }

    private fun setFilters(
        periods: List<PerformancePeriod>? = null,
        types: Set<String>? = null,
    ) {
        newState {
            copy(
                periods = periods?.map { PerformancePeriodUiModel(it) } ?: this.periods,
                types = types?.map { Type(it) }?.toSet()?.toList() ?: this.types,
            )
        }
    }

    fun updateFilter(filter: Filter<*>) {
//        newState {
//            if (!isLoading) {
//                when (filter) {
//                    is PerformancePeriodUiModel -> copy(
//                        periods = periods.updateFilter(filter) as List<PerformancePeriodUiModel>,
//                        currentFilters = currentFilters.addOrDeleteFilter(filter),
//                    )
//                    is Type -> copy(
//                        types = types.updateFilter(filter) as List<Type>,
//                        currentFilters = currentFilters.addOrDeleteFilter(filter),
//                    )
//                    is Name -> copy(
//                        name = filter.copy(isChecked = !filter.isChecked),
//                        currentFilters = currentFilters.addOrDeleteFilter(filter),
//                    )
//                }
//            } else {
//                this
//            }
//        }
    }

    fun resetFilters() {
        newState {
            copy(
                currentFilters = emptySet(),
                periods = periods?.map { it.copy(isChecked = false) },
                types = types.map { it.copy(isChecked = false) },
            )
        }
    }

    private fun List<GradePosition>.getExamTypes() = map { it.type }.toSet()

    // todo рефакторить и вынести в usecase
    private fun<T> List<Filter<T>>.updateFilter(newFilter: Filter<T>): Set<Filter<T>> {
        return emptySet()
//        val newSet = toMutableList()
//        newSet.forEachIndexed { index, filter ->
//            if (filter == newFilter) {
//                newSet[index] = when (newFilter) {
//                    is PerformancePeriodUiModel -> (newFilter as PerformancePeriodUiModel).copy(isChecked = !newFilter.isChecked)
//                    is Type -> (newFilter as Type).copy(isChecked = !newFilter.isChecked)
//                    is Name -> (newFilter as Name).copy(isChecked = !newFilter.isChecked)
//                } as Filter<T>
//            }
//        }
//        return newSet.toSet()
    }

    private fun<T> Set<Filter<T>>.addOrDeleteFilter(newFilter: Filter<T>): Set<Filter<T>> {
        val newSet = toMutableList()
        if (newFilter.isChecked) {
            if (newFilter is Name) {
                // TODO Поправить
                // newSet.removeIf { it is Name }
            }
            newSet.remove(newFilter)
        } else {
            val filter = when (newFilter) {
                is PerformancePeriodUiModel -> (newFilter as PerformancePeriodUiModel).copy(isChecked = !newFilter.isChecked)
                is Type -> (newFilter as Type).copy(isChecked = !newFilter.isChecked)
                is Name -> {
                    // TODO Поправить
                    // newSet.removeIf { it is Name }
                    (newFilter as Name).copy(isChecked = !newFilter.isChecked)
                }
            }
            newSet.add(filter as Filter<T>)
        }
        return newSet.toSet()
    }

    fun exit() {
        accountRouter.back()
    }
}

sealed class Filter<out T>(open val value: T, open val isChecked: Boolean) : ListItemUiModel() {

    abstract val mappedValue: String

    data class PerformancePeriodUiModel(
        val id: String,
        override val value: String,
        override val isChecked: Boolean = false,
        override val mappedValue: String = "$value курс",
    ) : Filter<String>(value, isChecked) {
        constructor(period: PerformancePeriod) : this(
            id = period.id,
            value = period.title,
        )

        override val listContentType: Any = 0
    }

    data class Type(
        override val value: String,
        override val isChecked: Boolean = false,
        override val mappedValue: String = value,
    ) : Filter<String>(value, isChecked) {
        override val listContentType: Any = 1
    }

    data class Name(
        override val value: String,
        override val isChecked: Boolean = false,
        override val mappedValue: String = value,
    ) : Filter<String>(value, isChecked) {
        override val listContentType: Any = 2
    }
}
