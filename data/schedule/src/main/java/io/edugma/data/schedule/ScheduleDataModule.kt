package io.edugma.data.schedule

import io.edugma.data.base.consts.DiConst
import io.edugma.data.base.utils.retrofit.buildRetrofitService
import io.edugma.data.schedule.api.FreePlacesService
import io.edugma.data.schedule.api.ScheduleInfoService
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.api.ScheduleSourcesService
import io.edugma.data.schedule.local.ScheduleLocalDS
import io.edugma.data.schedule.repository.*
import io.edugma.domain.schedule.repository.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ScheduleDataModule {
    val deps = module {
        single { buildRetrofitService<ScheduleService>(get(named(DiConst.Schedule))) }
        single { buildRetrofitService<ScheduleInfoService>(get(named(DiConst.Schedule))) }
        single { buildRetrofitService<ScheduleSourcesService>(get(named(DiConst.Schedule))) }
        single { buildRetrofitService<FreePlacesService>(get(named(DiConst.Schedule))) }
        single { ScheduleLocalDS(get()) }
        single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get(), get()) }
        single<ScheduleInfoRepository> { ScheduleInfoRepositoryImpl(get(), get()) }
        single<FreePlaceRepository> { FreePlaceRepositoryImpl(get()) }
        single<ScheduleSourcesRepository> { ScheduleSourcesRepositoryImpl(get(), get(), get()) }
        single<LessonsReviewRepository> { LessonsReviewRepositoryImpl(get()) }
    }
}