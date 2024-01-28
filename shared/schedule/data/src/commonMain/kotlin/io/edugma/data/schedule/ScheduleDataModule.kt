package io.edugma.data.schedule

import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.repository.FreePlaceRepositoryImpl
import io.edugma.data.schedule.repository.ScheduleCacheRepository
import io.edugma.data.schedule.repository.ScheduleInfoRepositoryImpl
import io.edugma.data.schedule.repository.ScheduleRepositoryImpl
import io.edugma.data.schedule.repository.ScheduleSourcesRepositoryImpl
import io.edugma.features.schedule.domain.repository.FreePlaceRepository
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object ScheduleDataModule {
    val deps = module {
        single { ScheduleService(get()) }
        singleOf(::ScheduleRepositoryImpl) { bind<ScheduleRepository>() }
        singleOf(::ScheduleCacheRepository)
        singleOf(::ScheduleInfoRepositoryImpl) { bind<ScheduleInfoRepository>() }
        singleOf(::FreePlaceRepositoryImpl) { bind<FreePlaceRepository>() }
        singleOf(::ScheduleSourcesRepositoryImpl) { bind<ScheduleSourcesRepository>() }
    }
}
