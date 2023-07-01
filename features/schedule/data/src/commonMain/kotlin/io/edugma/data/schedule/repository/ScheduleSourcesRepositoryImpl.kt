package io.edugma.data.schedule.repository

import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.consts.PrefConst
import io.edugma.data.schedule.api.ScheduleSourcesService
import io.edugma.domain.base.repository.CacheRepository
import io.edugma.domain.base.repository.SettingsRepository
import io.edugma.domain.base.repository.get
import io.edugma.domain.base.repository.getFlow
import io.edugma.domain.base.repository.save
import io.edugma.domain.base.repository.saveOrRemove
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
    private val scheduleSourcesService: ScheduleSourcesService,
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
) : ScheduleSourcesRepository {
    private val TAG = "ScheduleSourcesReposito"

    override fun getSourceTypes() =
        flow { emit(scheduleSourcesService.getSourceTypes()) }
            .flowOn(Dispatchers.IO)

    override suspend fun getSources(type: ScheduleSources) =
        scheduleSourcesService.getSources(type.name.lowercase()).getOrThrow()

    override suspend fun getFavoriteSources(): Flow<List<ScheduleSourceFull>> =
        cacheRepository.getFlow<List<ScheduleSourceFull>>(CacheConst.FavoriteScheduleSources)
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
