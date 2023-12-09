package io.edugma.features.account.marks

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.features.account.common.LOCAL_DATA_SHOWN_ERROR
import io.edugma.features.account.domain.model.performance.Performance
import io.edugma.features.account.domain.model.performance.PerformancePeriod
import io.edugma.features.account.domain.repository.PerformanceRepository
import io.edugma.features.account.marks.Filter.Name
import io.edugma.features.account.marks.Filter.PerformancePeriodUiModel
import io.edugma.features.account.marks.Filter.Type
import kotlinx.coroutines.async

class PerformanceViewModel(
    private val repository: PerformanceRepository,
    private val externalRouter: ExternalRouter,
) : BaseViewModel<MarksState>(MarksState()) {

    init {
        loadMarks(isUpdate = false)
    }

    fun loadMarks(isUpdate: Boolean = true) {
        launchCoroutine {
            setLoading(true)
            setError(false)

            val localMarks = async { repository.getLocalMarks() }
            val performancePeriods = async { repository.getPerformancePeriods() }
            // TODO Fix
            val marks = async { repository.getPerformance("2") }
            if (!isUpdate) {
                localMarks.await()?.let {
                    setFilters(
                        periods = repository.getLocalPerformancePeriods() ?: emptyList(),
                        types = it.getExamTypes(),
                    )
                    setPerformanceData(it)
                }
            }

            kotlin.runCatching {
                performancePeriods.await() to marks.await()
            }
                .onSuccess {
                    val performancePeriodsList = it.first
                    val performance = it.second
                    setPerformanceData(performance)
                    if (!isUpdate) {
                        setFilters(
                            periods = performancePeriodsList,
                            types = performance.getExamTypes(),
                        )
                    }
                }
                .onFailure {
                    setError(true)
                    if (localMarks.await().isNotNull()) {
                        externalRouter.showMessage(LOCAL_DATA_SHOWN_ERROR)
                    }
                }

            setLoading(false)
        }
    }

    fun openBottomSheetClick(performance: Performance?) {
        newState {
            copy(selectedPerformance = performance)
        }
    }

    private fun setPerformanceData(data: List<Performance>) {
        newState {
            copy(data = data)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        newState {
            copy(isLoading = isLoading)
        }
    }

    private fun setError(isError: Boolean) {
        newState {
            copy(isError = isError)
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
        newState {
            if (!isLoading) {
                when (filter) {
                    is PerformancePeriodUiModel -> copy(
                        periods = periods.updateFilter(filter) as List<PerformancePeriodUiModel>,
                        currentFilters = currentFilters.addOrDeleteFilter(filter),
                    )
                    is Type -> copy(
                        types = types.updateFilter(filter) as List<Type>,
                        currentFilters = currentFilters.addOrDeleteFilter(filter),
                    )
                    is Name -> copy(
                        name = filter.copy(isChecked = !filter.isChecked),
                        currentFilters = currentFilters.addOrDeleteFilter(filter),
                    )
                }
            } else {
                this
            }
        }
    }

    fun resetFilters() {
        newState {
            copy(
                currentFilters = emptySet(),
                periods = periods.map { it.copy(isChecked = false) },
                types = types.map { it.copy(isChecked = false) },
            )
        }
    }

    private fun List<Performance>.getExamTypes() = map { it.type }.toSet()

    // todo рефакторить и вынести в usecase
    private fun<T> List<Filter<T>>.updateFilter(newFilter: Filter<T>): Set<Filter<T>> {
        val newSet = toMutableList()
        newSet.forEachIndexed { index, filter ->
            if (filter == newFilter) {
                newSet[index] = when (newFilter) {
                    is PerformancePeriodUiModel -> (newFilter as PerformancePeriodUiModel).copy(isChecked = !newFilter.isChecked)
                    is Type -> (newFilter as Type).copy(isChecked = !newFilter.isChecked)
                    is Name -> (newFilter as Name).copy(isChecked = !newFilter.isChecked)
                } as Filter<T>
            }
        }
        return newSet.toSet()
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
}

data class MarksState(
    val data: List<Performance>? = null,
    val periods: List<PerformancePeriodUiModel> = emptyList(),
    val types: List<Type> = listOf(),
    val name: Name = Name(""),
    val currentFilters: Set<Filter<*>> = emptySet(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val selectedPerformance: Performance? = null,
) {
    val placeholders = data.isNull() && isLoading && !isError
    val bottomSheetPlaceholders = (isLoading && !isError) || (isError && data.isNull())
    val isRefreshing = data.isNotNull() && isLoading && !isError
    val showError
        get() = isError && data.isNull()
    val showNothingFound
        get() = filteredData?.isEmpty() == true

    private val filteredPeriods
        get() = periods.filter { it.isChecked }.toSet()

    private val filteredTypes
        get() = types.filter { it.isChecked }.toSet()

    private val enabledFilters
        get() = (filteredPeriods + filteredTypes).let {
            if (name.isChecked) it.plus(name) else it
        }

    val filteredData
        get() = data?.filter { performance ->
            when {
                enabledFilters.isEmpty() -> true
                else -> {
                    val type = Type(performance.type, true)
                    if (filteredTypes.isNotEmpty()) {
                        if (!filteredTypes.contains(type)) return@filter false
                    }
                    if (name.isChecked && !performance.title.contains(name.value, ignoreCase = true)) {
                        return@filter false
                    }
                    true
                }
            }
        }
}

sealed class Filter<out T>(open val value: T, open val isChecked: Boolean) {

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
    }

    data class Type(
        override val value: String,
        override val isChecked: Boolean = false,
        override val mappedValue: String = value,
    ) : Filter<String>(value, isChecked)

    data class Name(
        override val value: String,
        override val isChecked: Boolean = false,
        override val mappedValue: String = value,
    ) : Filter<String>(value, isChecked)
}
