package io.edugma.data.schedule.repository

import io.edugma.core.api.utils.LceFlow
import io.edugma.core.api.utils.lce
import io.edugma.core.api.utils.map
import io.edugma.data.base.store.store
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.features.schedule.domain.model.ScheduleComplexFilter
import io.edugma.features.schedule.domain.model.ScheduleRecord
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

class ScheduleRepositoryImpl(
    private val scheduleCacheRepository: ScheduleCacheRepository,
    private val scheduleService: ScheduleService,
) : ScheduleRepository {

    private val scheduleMockRepository = ScheduleMockRepository()
    override fun getRawSchedule(source: ScheduleSource): LceFlow<CompactSchedule> {
        return scheduleStore0.get(source, false)
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
            // expiresIn(1.days)
            expiresIn(10.seconds)
        }
        coroutineScope()
    }

    override fun getHistory(source: ScheduleSource): Flow<List<ScheduleRecord>> {
        // TODO
        TODO()
//        return scheduleCacheRepository.getScheduleHistory(source.id).map {
//            it?.map { ScheduleRecord(it.data.toModel(), it.timestamp) } ?: emptyList()
//        }
    }

    override suspend fun getHistoryRecord(
        source: ScheduleSource,
        timestamp: Instant,
    ): ScheduleRecord? {
        // TODO
        TODO()
//        val cacheRecord = scheduleCacheRepository.getScheduleHistoryRecord(source.id, timestamp)
//        return cacheRecord?.let {
//            ScheduleRecord(
//                schedule = cacheRecord.data.toModel(),
//                timestamp = cacheRecord.timestamp,
//            )
//        }
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
                    result = scheduleService.getComplexSchedule(
                        ScheduleComplexFilter(),
                    ),
                    isLoading = false,
                )
            }.map { ScheduleCalendar(it) }
        } else {
            scheduleStore0.get(source, forceUpdate)
                .map { ScheduleCalendar(it) }
        }
    }
}
