package io.edugma.features.schedule.lessons_review

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel { LessonsReviewViewModel(get()) }
}