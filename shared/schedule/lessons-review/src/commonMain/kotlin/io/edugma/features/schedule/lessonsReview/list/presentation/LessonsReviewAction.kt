package io.edugma.features.schedule.lessonsReview.list.presentation

sealed interface LessonsReviewAction {
    data object OnRefresh : LessonsReviewAction
}
