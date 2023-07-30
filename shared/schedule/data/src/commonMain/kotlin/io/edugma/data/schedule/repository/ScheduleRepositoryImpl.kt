package io.edugma.data.schedule.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getFlow
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.Lce
import io.edugma.core.api.utils.loading
import io.edugma.core.api.utils.map
import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.store.store
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.model.toModel
import io.edugma.features.schedule.domain.model.ScheduleComplexFilter
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.seconds

class ScheduleRepositoryImpl(
    private val scheduleService: ScheduleService,
    private val cacheRepository: CacheRepository,
) : ScheduleRepository {

    private val scheduleMockRepository = ScheduleMockRepository()
    override fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>> {
        return scheduleStore0.get(source, false)
    }

    private val scheduleStore0 = store<ScheduleSource, CompactSchedule> {
        // fetcher { key -> scheduleService.getCompactSchedule(key).getOrThrow() }
        fetcher { key -> scheduleMockRepository.getSuspendMockSchedule().getOrThrow() }
        cache {
            reader { key ->
                cacheRepository.getFlow<CompactSchedule>(CacheConst.Schedule + key.id)
            }
            writer { key, data ->
                cacheRepository.save(CacheConst.Schedule + key.id, data)
            }
            // expiresIn(1.days)
            expiresIn(10.seconds)
        }
        coroutineScope()
    }

    override fun getHistory(source: ScheduleSource) =
        flowOf(
            kotlin.runCatching {
//                localDS.getAll(source)
//                    .associate { it.date to (it.days?.toModel() ?: emptyList()) }
                null!!
            },
        )

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
