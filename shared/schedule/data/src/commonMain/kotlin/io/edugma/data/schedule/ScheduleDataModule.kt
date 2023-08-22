package io.edugma.data.schedule

import de.jensklingenberg.ktorfit.Ktorfit
import io.edugma.data.base.consts.DiConst
import io.edugma.data.schedule.api.FreePlacesService
import io.edugma.data.schedule.api.ScheduleInfoService
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.api.ScheduleSourcesService
import io.edugma.data.schedule.repository.FreePlaceRepositoryImpl
import io.edugma.data.schedule.repository.LessonsReviewRepositoryImpl
import io.edugma.data.schedule.repository.ScheduleCacheRepository
import io.edugma.data.schedule.repository.ScheduleInfoRepositoryImpl
import io.edugma.data.schedule.repository.ScheduleRepositoryImpl
import io.edugma.data.schedule.repository.ScheduleSourcesRepositoryImpl
import io.edugma.features.schedule.domain.repository.FreePlaceRepository
import io.edugma.features.schedule.domain.repository.LessonsReviewRepository
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ScheduleDataModule {
    val deps = module {
        single { get<Ktorfit>(named(DiConst.Schedule)).create<ScheduleService>() }
        single { get<Ktorfit>(named(DiConst.Schedule)).create<ScheduleInfoService>() }
        single { get<Ktorfit>(named(DiConst.Schedule)).create<ScheduleSourcesService>() }
        single { get<Ktorfit>(named(DiConst.Schedule)).create<FreePlacesService>() }
        singleOf(::ScheduleRepositoryImpl) { bind<ScheduleRepository>() }
        singleOf(::ScheduleCacheRepository)
        single<ScheduleInfoRepository> { ScheduleInfoRepositoryImpl(get(), get()) }
        single<FreePlaceRepository> { FreePlaceRepositoryImpl(get()) }
        single<ScheduleSourcesRepository> { ScheduleSourcesRepositoryImpl(get(), get(), get()) }
        single<LessonsReviewRepository> { LessonsReviewRepositoryImpl(get()) }
    }
}
