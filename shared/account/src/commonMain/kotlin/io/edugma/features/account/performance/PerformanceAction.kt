package io.edugma.features.account.performance

sealed interface PerformanceAction {
    data class OnPeriodSelected(
        val period: Filter.PerformancePeriodUiModel,
    ) : PerformanceAction

    data object OnRetryClicked : PerformanceAction
}
