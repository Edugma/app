package io.edugma.data.schedule.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.SettingsRepository
import io.edugma.core.api.repository.get
import io.edugma.core.api.repository.getDataFlow
import io.edugma.core.api.repository.getFlow
import io.edugma.core.api.repository.save
import io.edugma.core.api.repository.saveOrRemove
import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.consts.PrefConst
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ScheduleSourcesRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
    private val scheduleService: ScheduleService,
) : ScheduleSourcesRepository {

    override fun getSourceTypes() =
        flow { emit(scheduleService.getSourceTypes()) }
            .flowOn(Dispatchers.IO)

    override suspend fun getSources(
        type: ScheduleSources,
        query: String,
        limit: Int,
        page: String?,
    ): List<ScheduleSourceFull> {
        return scheduleService.getSources(
            type = type.name.lowercase(),
            query = query,
            limit = limit,
            page = page,
        )
    }

    override suspend fun getFavoriteSources(): Flow<List<ScheduleSourceFull>> =
        cacheRepository.getDataFlow<List<ScheduleSourceFull>>(CacheConst.FavoriteScheduleSources)
            .map { it ?: emptyList() }

    override suspend fun setFavoriteSources(sources: List<ScheduleSourceFull>) {
        cacheRepository.save(CacheConst.FavoriteScheduleSources, sources)
    }

    override suspend fun setSelectedSource(source: ScheduleSourceFull?) {
        settingsRepository.saveOrRemove(PrefConst.SelectedScheduleSource, source)
    }

    override fun getSelectedSource(): Flow<ScheduleSourceFull?> {
        return settingsRepository.getFlow(PrefConst.SelectedScheduleSource)
    }

    override suspend fun getSelectedSourceSuspend(): ScheduleSourceFull? {
        return settingsRepository.get(PrefConst.SelectedScheduleSource)
    }
}
