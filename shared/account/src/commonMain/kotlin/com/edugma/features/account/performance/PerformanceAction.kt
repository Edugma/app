package com.edugma.features.account.performance

import com.edugma.features.account.domain.model.performance.GradePosition

sealed interface PerformanceAction {
    data class OnPeriodSelected(
        val period: Filter.PerformancePeriodUiModel,
    ) : PerformanceAction

    data class OnPerformanceClicked(val gradePosition: GradePosition?) : PerformanceAction

    data object OnRefresh : PerformanceAction
}
