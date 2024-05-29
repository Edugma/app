package com.edugma.features.schedule.menu.presentation

sealed interface ScheduleMenuAction {
    data object OnScheduleClick : ScheduleMenuAction
    data object OnLessonsReviewClick : ScheduleMenuAction
    data object OnScheduleCalendarClick : ScheduleMenuAction
    data object OnScheduleSourceClick : ScheduleMenuAction
    data object OnFreePlaceClick : ScheduleMenuAction
    data object OnAppWidgetClick : ScheduleMenuAction
    data object OnHistoryClick : ScheduleMenuAction
    data object OnSignOut : ScheduleMenuAction
}
