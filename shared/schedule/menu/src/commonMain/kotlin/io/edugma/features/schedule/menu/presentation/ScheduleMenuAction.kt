package io.edugma.features.schedule.menu.presentation

sealed interface ScheduleMenuAction {
    object OnScheduleClick : ScheduleMenuAction
    object OnLessonsReviewClick : ScheduleMenuAction
    object OnScheduleCalendarClick : ScheduleMenuAction
    object OnScheduleSourceClick : ScheduleMenuAction
    object OnFreePlaceClick : ScheduleMenuAction
    object OnAppWidgetClick : ScheduleMenuAction
    object OnHistoryClick : ScheduleMenuAction
    object OnSignOut : ScheduleMenuAction
}
