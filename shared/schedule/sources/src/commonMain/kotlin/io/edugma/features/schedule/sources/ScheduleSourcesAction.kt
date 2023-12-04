package io.edugma.features.schedule.sources

sealed interface ScheduleSourcesAction {
    data object OnLoadPage : ScheduleSourcesAction
}
