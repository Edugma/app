package io.edugma.features.schedule.lessonsReview

sealed interface LessonsReviewAction {
    data object OnRefresh : LessonsReviewAction
}
