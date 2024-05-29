package com.edugma.features.schedule.sources

import com.edugma.features.schedule.domain.model.source.ScheduleSourceType
import com.edugma.features.schedule.sources.model.ScheduleSourceUiModel

sealed interface ScheduleSourcesAction {
    data object OnLoadPage : ScheduleSourcesAction

    data object OnRefresh : ScheduleSourcesAction

    data class OnTabSelected(
        val type: ScheduleSourceType,
    ) : ScheduleSourcesAction

    data class OnSourceSelected(
        val source: ScheduleSourceUiModel,
    ) : ScheduleSourcesAction

    data class OnAddToFavorite(
        val source: ScheduleSourceUiModel,
    ) : ScheduleSourcesAction

    data class OnDeleteFromFavorite(
        val source: ScheduleSourceUiModel,
    ) : ScheduleSourcesAction

    data class OnSourceClicked(
        val source: ScheduleSourceUiModel,
    ) : ScheduleSourcesAction
}
