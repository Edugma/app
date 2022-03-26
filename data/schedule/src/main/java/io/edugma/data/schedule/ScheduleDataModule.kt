package io.edugma.data.schedule

import io.edugma.data.base.consts.DiConst
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.local.ScheduleLocalDS
import io.edugma.data.schedule.repository.ScheduleRepositoryImpl
import io.edugma.domain.schedule.repository.ScheduleRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val scheduleDataModule = module {
    single { get<Retrofit>(named(DiConst.Schedule)).create(ScheduleService::class.java) }
    single { ScheduleLocalDS(get()) }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get(), get()) }
}