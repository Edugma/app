package com.edugma.data.schedule.repository

import com.edugma.core.api.utils.LceFlow
import com.edugma.core.api.utils.lce
import com.edugma.core.api.utils.map
import com.edugma.core.api.utils.runCoCatching
import com.edugma.data.base.store.store
import com.edugma.data.schedule.api.ScheduleService
import com.edugma.features.schedule.domain.model.ScheduleComplexFilter
import com.edugma.features.schedule.domain.model.ScheduleRecord
import com.edugma.features.schedule.domain.model.compact.CompactSchedule
import com.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import com.edugma.features.schedule.domain.model.source.ScheduleSource
import com.edugma.features.schedule.domain.model.source.ScheduleSourceType
import com.edugma.features.schedule.domain.model.teacher.TeacherInfo
import com.edugma.features.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

class ScheduleRepositoryImpl(
    private val scheduleCacheRepository: ScheduleCacheRepository,
    private val scheduleService: ScheduleService,
) : ScheduleRepository {

    private val scheduleMockRepository = ScheduleMockRepository()
    override fun getRawSchedule(
        source: ScheduleSource,
        forceUpdate: Boolean,
    ): LceFlow<CompactSchedule> {
        return scheduleStore0.get(source, forceUpdate)
    }

    private val scheduleStore0 = store<ScheduleSource, CompactSchedule> {
        fetcher { key ->
            scheduleService.getCompactSchedule(
                type = key.type,
                key = key.key,
            )
        }
        // fetcher { key -> scheduleMockRepository.getSuspendMockSchedule().getOrThrow() }
        cache {
            reader { key ->
                scheduleCacheRepository.getSchedule(key.id)
            }
            writer { key, data ->
                scheduleCacheRepository.saveSchedule(key.id, data)
            }
            expiresIn(1.days)
        }
        coroutineScope()
    }

    override fun getHistory(source: ScheduleSource): Flow<List<ScheduleRecord>> {
        return scheduleCacheRepository.getScheduleHistory(source.id).map {
            it?.map { ScheduleRecord(it.data, it.timestamp) } ?: emptyList()
        }
    }

    override suspend fun getHistoryRecord(
        source: ScheduleSource,
        timestamp: Instant,
    ): ScheduleRecord? {
        val cacheRecord = scheduleCacheRepository.getScheduleHistoryRecord(source.id, timestamp)
        return cacheRecord?.let {
            ScheduleRecord(
                schedule = cacheRecord.data,
                timestamp = cacheRecord.timestamp,
            )
        }
    }

    override suspend fun clearAll() {
        scheduleCacheRepository.clearAll()
    }

    // TODO
    override fun getTeacher(source: ScheduleSource, id: String) =
        scheduleStore0.get(source)
            .map { it?.attendees?.firstOrNull { it.id == id } as? TeacherInfo }

    override fun getSchedule(
        source: ScheduleSource,
        forceUpdate: Boolean,
    ): LceFlow<ScheduleCalendar> {
        return if (source.type == ScheduleSourceType.COMPLEX) {
            lce {
                emitResult(
                    result = runCoCatching {
                        scheduleService.getComplexSchedule(ScheduleComplexFilter())
                    },
                    isLoading = false,
                )
            }.map { ScheduleCalendar(it) }
        } else {
            scheduleStore0.get(source, forceUpdate)
                .map { ScheduleCalendar(it) }
        }
    }
}
