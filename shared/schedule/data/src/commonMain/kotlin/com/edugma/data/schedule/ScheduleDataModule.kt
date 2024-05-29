package com.edugma.data.schedule

import com.edugma.data.schedule.api.ScheduleService
import com.edugma.data.schedule.repository.FreePlaceRepositoryImpl
import com.edugma.data.schedule.repository.ScheduleCacheRepository
import com.edugma.data.schedule.repository.ScheduleInfoRepositoryImpl
import com.edugma.data.schedule.repository.ScheduleRepositoryImpl
import com.edugma.data.schedule.repository.ScheduleSourcesRepositoryImpl
import com.edugma.features.schedule.domain.repository.FreePlaceRepository
import com.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import com.edugma.features.schedule.domain.repository.ScheduleRepository
import com.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
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
