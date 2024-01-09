package io.edugma.features.account.performance

import androidx.compose.runtime.Immutable
import io.edugma.core.api.model.LceUiState
import io.edugma.core.utils.isNull
import io.edugma.features.account.domain.model.performance.GradePosition
import io.edugma.features.account.domain.model.performance.Performance
import io.edugma.features.account.domain.model.performance.PerformanceDto

@Immutable
data class PerformanceUiState(
    val lceState: LceUiState = LceUiState.init(),
    val performanceList: Performance? = null,
    val periods: List<Filter.PerformancePeriodUiModel>? = null,
    val selectedPeriod: Filter.PerformancePeriodUiModel? = null,
    val types: List<Filter.Type> = listOf(),
    val name: Filter.Name = Filter.Name(""),
    val currentFilters: Set<Filter<*>> = emptySet(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val selectedPerformance: GradePosition? = null,
) {
    val bottomSheetPlaceholders = (isLoading && !isError) || (isError && performanceList.isNull())

    private val filteredPeriods
        get() = periods?.filter { it.isChecked }?.toSet() ?: emptySet()

    private val filteredTypes
        get() = types.filter { it.isChecked }.toSet()

    private val enabledFilters
        get() = (filteredPeriods + filteredTypes).let {
            if (name.isChecked) it.plus(name) else it
        }

    val filteredData
        get() = performanceList?.grades?.filter { performance ->
            when {
                enabledFilters.isEmpty() -> true
                else -> {
                    val type = Filter.Type(performance.type, true)
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

    fun toContent(data: PerformanceDto): PerformanceUiState {
        val selectedPeriod = if (data.selected?.id != selectedPeriod?.id) {
            data.periods.firstOrNull { it.id == data.selected?.id }?.let {
                Filter.PerformancePeriodUiModel(it)
            }
        } else {
            selectedPeriod
        }

        return copy(
            selectedPeriod = selectedPeriod,
            performanceList = data.selected,
            periods = data.periods.map { Filter.PerformancePeriodUiModel(it) },
        )
    }

    fun toPeriodSelected(period: Filter.PerformancePeriodUiModel): PerformanceUiState {
        return copy(
            selectedPeriod = period,
            performanceList = null,
        )
    }
}
