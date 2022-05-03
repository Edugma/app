package io.edugma.domain.schedule

import io.edugma.domain.schedule.usecase.LessonsReviewUseCase
import io.edugma.domain.schedule.usecase.ScheduleSourcesUseCase
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import org.koin.dsl.module

object ScheduleDomainModule {
    val deps = module {
        factory { ScheduleUseCase(get(), get()) }
        factory { ScheduleSourcesUseCase(get()) }
        factory { LessonsReviewUseCase(get(), get()) }
    }
}