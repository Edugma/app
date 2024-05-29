package com.edugma.features.schedule.domain

import com.edugma.features.schedule.domain.usecase.GetClosestLessonsUseCase
import com.edugma.features.schedule.domain.usecase.GetCurrentScheduleUseCase
import com.edugma.features.schedule.domain.usecase.IsScheduleSourceSelectedUseCase
import com.edugma.features.schedule.domain.usecase.RemoveSelectedScheduleSourceUseCase
import com.edugma.features.schedule.domain.usecase.ScheduleHistoryUseCase
import com.edugma.features.schedule.domain.usecase.ScheduleSourcesUseCase
import com.edugma.features.schedule.domain.usecase.ScheduleUseCase
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
