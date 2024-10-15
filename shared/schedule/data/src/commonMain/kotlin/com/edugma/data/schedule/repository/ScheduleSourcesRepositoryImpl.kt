package com.edugma.data.schedule.repository

import com.edugma.core.api.model.PagingDto
import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.api.repository.get
import com.edugma.core.api.repository.getFlow
import com.edugma.core.api.repository.getOnlyData
import com.edugma.core.api.repository.save
import com.edugma.core.api.repository.saveOrRemove
import com.edugma.data.base.consts.CacheConst
import com.edugma.data.base.consts.PrefConst
import com.edugma.data.schedule.api.ScheduleService
import com.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import com.edugma.features.schedule.domain.model.source.ScheduleSourceType
import com.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.Flow

class ScheduleSourcesRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
    private val scheduleService: ScheduleService,
) : ScheduleSourcesRepository {

    override suspend fun getSourceTypes(): List<ScheduleSourceType> {
        return scheduleService.getSourceTypes()
    }

    override suspend fun getSources(
        type: ScheduleSourceType,
        query: String,
        limit: Int,
        page: String?,
    ): PagingDto<ScheduleSourceFull> {
        return scheduleService.getSources(
            type = type.id,
            query = query,
            limit = limit,
            page = page,
        )
    }

    override suspend fun getFavoriteSources(): List<ScheduleSourceFull> {
        return cacheRepository.getOnlyData<List<ScheduleSourceFull>>(
            CacheConst.FavoriteScheduleSources,
        ) ?: emptyList()
    }

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

    override suspend fun clearAll() {
        settingsRepository.removeObject(PrefConst.SelectedScheduleSource)
        cacheRepository.removeWithPrefix(CacheConst.FavoriteScheduleSources)
    }
}
