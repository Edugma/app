package io.edugma.features.schedule.sources

sealed interface ScheduleSourcesAction {
    data object OnLoadPage : ScheduleSourcesAction
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
