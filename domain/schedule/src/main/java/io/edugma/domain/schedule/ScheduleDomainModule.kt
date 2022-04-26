package io.edugma.domain.schedule

import io.edugma.domain.schedule.usecase.ScheduleUseCase
import org.koin.dsl.module

object ScheduleDomainModule {
    val module = module {
        single { ScheduleUseCase(get()) }
    }
}