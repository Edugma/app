package io.edugma.data.schedule

import io.edugma.data.base.consts.DiConst
import io.edugma.data.base.utils.retrofit.buildRetrofitService
import io.edugma.data.schedule.api.FreePlacesService
import io.edugma.data.schedule.api.ScheduleInfoService
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.api.ScheduleSourcesService
import io.edugma.data.schedule.local.ScheduleLocalDS
import io.edugma.data.schedule.repository.FreePlaceRepositoryImpl
import io.edugma.data.schedule.repository.ScheduleInfoRepositoryImpl
import io.edugma.data.schedule.repository.ScheduleRepositoryImpl
import io.edugma.domain.schedule.repository.FreePlaceRepository
import io.edugma.domain.schedule.repository.ScheduleInfoRepository
import io.edugma.domain.schedule.repository.ScheduleRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val module = module {
    single { buildRetrofitService<ScheduleService>(get(named(DiConst.Schedule))) }
    single { buildRetrofitService<ScheduleInfoService>(get(named(DiConst.Schedule))) }
    single { buildRetrofitService<ScheduleSourcesService>(get(named(DiConst.Schedule))) }
    single { buildRetrofitService<FreePlacesService>(get(named(DiConst.Schedule))) }
    single { ScheduleLocalDS(get()) }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get(), get(), get()) }
    single<ScheduleInfoRepository> { ScheduleInfoRepositoryImpl(get(), get()) }
    single<FreePlaceRepository> { FreePlaceRepositoryImpl(get()) }
}