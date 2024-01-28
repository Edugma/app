package io.edugma.features.schedule.domain

import io.edugma.features.schedule.domain.usecase.GetClosestLessonsUseCase
import io.edugma.features.schedule.domain.usecase.GetCurrentScheduleUseCase
import io.edugma.features.schedule.domain.usecase.IsScheduleSourceSelectedUseCase
import io.edugma.features.schedule.domain.usecase.RemoveSelectedScheduleSourceUseCase
import io.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase
import io.edugma.features.schedule.domain.usecase.ScheduleSourcesUseCase
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleDomainModule {
    val deps = module {
        factoryOf(::ScheduleUseCase)
        factoryOf(::ScheduleSourcesUseCase)
        factoryOf(::ScheduleHistoryUseCase)
        factoryOf(::GetClosestLessonsUseCase)
        factoryOf(::IsScheduleSourceSelectedUseCase)
        factoryOf(::RemoveSelectedScheduleSourceUseCase)
        factoryOf(::GetCurrentScheduleUseCase)
    }
}
