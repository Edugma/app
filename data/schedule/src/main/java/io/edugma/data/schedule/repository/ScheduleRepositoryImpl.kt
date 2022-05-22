package io.edugma.data.schedule.repository

import android.util.Log
import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.local.*
import io.edugma.data.base.model.map
import io.edugma.data.base.store.Store
import io.edugma.data.base.store.StoreImpl
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.api.getCompactSchedule
import io.edugma.data.schedule.local.ScheduleLocalDS
import io.edugma.data.schedule.model.ScheduleDao
import io.edugma.data.schedule.model.ScheduleKey
import io.edugma.data.schedule.model.toModel
import io.edugma.domain.base.utils.Lce
import io.edugma.domain.base.utils.TAG
import io.edugma.domain.base.utils.map
import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

class ScheduleRepositoryImpl(
    private val scheduleService: ScheduleService,
    private val localDS: ScheduleLocalDS,
    private val dataVersionLocalDS: DataVersionLocalDS
) : ScheduleRepository {
    override fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>> {
        return scheduleStore0.get(source, false)
    }

    private val scheduleStore0 = Store<ScheduleSource, CompactSchedule>(
        fetcher = { key -> scheduleService.getCompactSchedule(key) },
        reader = { key ->
            dataVersionLocalDS.getFlow(key.id, expireAt) {
                kotlin.runCatching { localDS.getLast(it) }
                    .onFailure { Log.e(ScheduleRepositoryImpl::class.TAG, "Error", it) }
            }.map { it.map { it?.days } }
        },
        writer = { key, data ->
            flowOf(
                dataVersionLocalDS.save(
                    ScheduleDao.from(key, data),
                    key.id
                ) { dao, _ ->
                    kotlin.runCatching { localDS.add(dao) }
                        .onFailure { Log.e(ScheduleRepositoryImpl::class.TAG, "Error", it) }
                })
        },
        expireAt = 1.days
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
        flowOf(kotlin.runCatching {
            localDS.getAll(source).associate { it.date to (it.days?.toModel() ?: emptyList()) }
        })

    override fun getSchedule(source: ScheduleSource, forceUpdate: Boolean) =
        scheduleStore0.get(source, forceUpdate)
            .map { it.map { it?.toModel() ?: emptyList() } }
            .flowOn(Dispatchers.IO)
}