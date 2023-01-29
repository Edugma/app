package io.edugma.data.schedule.repository

import io.edugma.data.base.local.DataVersionLocalDS
import io.edugma.data.base.local.getFlow
import io.edugma.data.base.local.save
import io.edugma.data.base.model.map
import io.edugma.data.base.store.Store
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.api.getCompactSchedule
import io.edugma.data.schedule.local.ScheduleLocalDS
import io.edugma.data.schedule.model.ScheduleDao
import io.edugma.data.schedule.model.toModel
import io.edugma.domain.base.utils.Lce
import io.edugma.domain.base.utils.loading
import io.edugma.domain.base.utils.map
import io.edugma.features.schedule.domain.model.ScheduleComplexFilter
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.days

class ScheduleRepositoryImpl(
    private val scheduleService: ScheduleService,
    private val localDS: ScheduleLocalDS,
    private val dataVersionLocalDS: DataVersionLocalDS,
) : ScheduleRepository {
    override fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>> {
        return scheduleStore0.get(source, false)
    }

    private val scheduleStore0 = Store<ScheduleSource, CompactSchedule>(
        fetcher = { key -> scheduleService.getCompactSchedule(key) },
        reader = { key ->
            dataVersionLocalDS.getFlow(key.id, expireAt) {
                kotlin.runCatching { localDS.getLast(it) }
            }.map { it.map { it?.days } }
        },
        writer = { key, data ->
            flowOf(
                dataVersionLocalDS.save(
                    ScheduleDao.from(key, data),
                    key.id,
                ) { dao, _ ->
                    kotlin.runCatching { localDS.add(dao) }
                },
            )
        },
        expireAt = 1.days,
    )

//    private val scheduleStore2 = Store<ScheduleKey, CompactSchedule>(
//        fetcher = { key -> scheduleService.getCompactSchedule(key.source) },
//        reader = { key ->
//            dataVersionLocalDS.getFlow(CacheConst.Schedule + key.toString(), expireAt) {
//                kotlin.runCatching { localDS.getLast(it) }
//            }.map { it.map { it?.days } }
//        },
//        writer = { key, data ->
//            flowOf(
//                dataVersionLocalDS.save(
//                    ScheduleDao.from(key.source, data),
//                    CacheConst.Schedule + key
//                ) { dao, _ ->
//                    kotlin.runCatching { localDS.add(dao) }
//                })
//        },
//        expireAt = 1.days
//    )

    override fun getHistory(source: ScheduleSource) =
        flowOf(
            kotlin.runCatching {
                localDS.getAll(source)
                    .associate { it.date to (it.days?.toModel() ?: emptyList()) }
            },
        )

    override fun getTeacher(source: ScheduleSource, id: String) =
        scheduleStore0.get(source)
            .map { it.map { it?.info?.teachersInfo?.firstOrNull { it.id == id } }.getOrNull() }
            .flowOn(Dispatchers.IO)

    override fun getSchedule(source: ScheduleSource, forceUpdate: Boolean):
        Flow<Lce<List<ScheduleDay>>> =
        if (source.type == ScheduleSources.Complex) {
            scheduleService.getComplexSchedule(
                ScheduleComplexFilter(),
            ).map { it.loading(false).map { it.toModel() } }
                .flowOn(Dispatchers.IO)
        } else {
            scheduleStore0.get(source, forceUpdate)
                .map { it.map { it?.toModel() ?: emptyList() } }
                .flowOn(Dispatchers.IO)
        }
}
