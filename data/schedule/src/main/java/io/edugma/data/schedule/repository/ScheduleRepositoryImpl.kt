package io.edugma.data.schedule.repository

import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.consts.PrefConst
import io.edugma.data.base.local.*
import io.edugma.data.base.model.map
import io.edugma.data.base.store.StoreImpl
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.model.ScheduleDao
import io.edugma.domain.base.utils.loading
import io.edugma.domain.base.utils.map
import io.edugma.domain.schedule.model.place.PlaceFilters
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

class ScheduleRepositoryImpl(
    private val service: ScheduleService,
    private val cachedDS: CacheVersionLocalDS,
    private val preferencesDS: PreferencesDS
) : ScheduleRepository {
    override fun getSourceTypes() =
        service.getSourceTypes()
            .flowOn(Dispatchers.IO)

    override fun getSources(type: ScheduleSources) =
        service.getSources(type.name.lowercase())
            .flowOn(Dispatchers.IO)

    override suspend fun setSelectedSource(source: ScheduleSourceFull) {
        preferencesDS.set(source, PrefConst.SelectedScheduleSource)
    }

    override fun getSelectedSource() = preferencesDS
        .flowOf<ScheduleSourceFull>(PrefConst.SelectedScheduleSource)
        .flowOn(Dispatchers.IO)


    private val scheduleStore = StoreImpl<ScheduleSource, List<ScheduleDay>>(
        fetcher = { key -> service.getSchedule(key.type.name.lowercase(), key.key) },
        reader = { key ->
            cachedDS.getFlow<ScheduleDao>(CacheConst.Schedule + key, expireAt)
                .map { it.map { it?.days } }
        },
        writer = { key, data ->
            flowOf(cachedDS.save(ScheduleDao.from(key, data), CacheConst.Schedule + key))
        },
        expireAt = 1.days
    )

    override fun getSchedule(source: ScheduleSource, forceUpdate: Boolean) =
        scheduleStore.get(source, forceUpdate)
            .map { it.map { it ?: emptyList() } }
            .flowOn(Dispatchers.IO)

    override fun getLessonsReview(source: ScheduleSource) =
        service.getLessonsReview(source.type.name.lowercase(), source.key)
            .flowOn(Dispatchers.IO)

    override fun findFreePlaces(filters: PlaceFilters) =
        service.findFreePlaces(filters)
            .flowOn(Dispatchers.IO)
}