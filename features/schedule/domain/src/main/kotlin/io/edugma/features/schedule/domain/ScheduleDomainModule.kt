package io.edugma.features.schedule.domain

import io.edugma.features.schedule.domain.usecase.LessonsReviewUseCase
import io.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase
import io.edugma.features.schedule.domain.usecase.ScheduleSourcesUseCase
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import org.koin.dsl.module

object ScheduleDomainModule {
    val deps = module {
        factory { ScheduleUseCase(get(), get()) }
        factory { ScheduleSourcesUseCase(get()) }
        factory { LessonsReviewUseCase(get(), get()) }
        factory { ScheduleHistoryUseCase(get(), get()) }
    }
}
