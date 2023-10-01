package io.edugma.data.schedule.repository

import io.edugma.core.api.utils.Lce
import io.edugma.core.api.utils.loading
import io.edugma.core.api.utils.map
import io.edugma.data.base.store.store
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.model.toModel
import io.edugma.features.schedule.domain.model.ScheduleComplexFilter
import io.edugma.features.schedule.domain.model.ScheduleRecord
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

class ScheduleRepositoryImpl(
    private val scheduleService: ScheduleService,
    private val scheduleCacheRepository: ScheduleCacheRepository,
) : ScheduleRepository {

    private val scheduleMockRepository = ScheduleMockRepository()
    override fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>> {
        return scheduleStore0.get(source, false)
    }

    private val scheduleStore0 = store<ScheduleSource, CompactSchedule> {
//        fetcher { key ->
//            scheduleService.getCompactSchedule(
//                type = key.type.name.lowercase(),
//                key = key.key,
//            ).getOrThrow()
//        }
        fetcher { key -> scheduleMockRepository.getSuspendMockSchedule().getOrThrow() }
        cache {
            reader { key ->
                scheduleCacheRepository.getSchedule(key.id)
            }
            writer { key, data ->
                scheduleCacheRepository.saveSchedule(key.id, data)
            }
            // expiresIn(1.days)
            expiresIn(10.seconds)
        }
        coroutineScope()
    }

    override fun getHistory(source: ScheduleSource): Flow<List<ScheduleRecord>> {
        return scheduleCacheRepository.getScheduleHistory(source.id).map {
            it?.map { ScheduleRecord(it.data.toModel(), it.timestamp) } ?: emptyList()
        }
    }

    override suspend fun getHistoryRecord(
        source: ScheduleSource,
        timestamp: Instant,
    ): ScheduleRecord? {
        val cacheRecord = scheduleCacheRepository.getScheduleHistoryRecord(source.id, timestamp)
        return cacheRecord?.let {
            ScheduleRecord(
                schedule = cacheRecord.data.toModel(),
                timestamp = cacheRecord.timestamp,
            )
        }
    }

    override fun getTeacher(source: ScheduleSource, id: String) =
        scheduleStore0.get(source)
            .map { it.map { it?.info?.teachersInfo?.firstOrNull { it.id == id } }.getOrNull() }
            .flowOn(Dispatchers.IO)

    override fun getSchedule(source: ScheduleSource, forceUpdate: Boolean):
        Flow<Lce<List<ScheduleDay>>> =
        if (source.type == ScheduleSources.Complex) {
            flow {
                emit(
                    scheduleService.getComplexSchedule(
                        ScheduleComplexFilter(),
                    ),
                )
            }
                .map { it.loading(false).map { it.toModel() } }
                .flowOn(Dispatchers.IO)
        } else {
            scheduleStore0.get(source, forceUpdate)
                .map { it.map { it?.toModel() ?: emptyList() } }
                .flowOn(Dispatchers.IO)
        }
}
