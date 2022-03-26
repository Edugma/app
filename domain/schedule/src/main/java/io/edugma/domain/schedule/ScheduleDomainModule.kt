package io.edugma.domain.schedule

import io.edugma.domain.schedule.usecase.ScheduleUseCase
import org.koin.dsl.module

val scheduleDomainModule = module {
    single { ScheduleUseCase(get()) }
}