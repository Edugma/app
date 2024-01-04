package io.edugma.features.account.performance

import androidx.compose.runtime.Immutable
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.features.account.domain.model.performance.Performance
import io.edugma.features.account.domain.model.performance.PerformanceApi

@Immutable
data class PerformanceUiState(
    val performanceList: List<Performance>? = null,
    val periods: List<Filter.PerformancePeriodUiModel>? = null,
    val selectedPeriod: Filter.PerformancePeriodUiModel? = null,
    val types: List<Filter.Type> = listOf(),
    val name: Filter.Name = Filter.Name(""),
    val currentFilters: Set<Filter<*>> = emptySet(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val selectedPerformance: Performance? = null,
) {
    val placeholders = performanceList.isNull() && isLoading && !isError
    val bottomSheetPlaceholders = (isLoading && !isError) || (isError && performanceList.isNull())
    val isRefreshing = performanceList.isNotNull() && isLoading && !isError
    val showError
        get() = isError && performanceList.isNull()
    val showNothingFound
        get() = filteredData?.isEmpty() == true

    private val filteredPeriods
        get() = periods?.filter { it.isChecked }?.toSet() ?: emptySet()

    private val filteredTypes
        get() = types.filter { it.isChecked }.toSet()

    private val enabledFilters
        get() = (filteredPeriods + filteredTypes).let {
            if (name.isChecked) it.plus(name) else it
        }

    val filteredData
        get() = performanceList?.filter { performance ->
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

    fun toContent(data: PerformanceApi) =
        copy(
            performanceList = data.selected,
            periods = data.periods.map { Filter.PerformancePeriodUiModel(it) },
        )

    fun toError(isError: Boolean) = copy(isError = isError)

    fun toLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}
