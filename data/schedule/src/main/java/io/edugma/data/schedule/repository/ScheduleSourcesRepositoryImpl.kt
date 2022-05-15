package io.edugma.data.schedule.repository

import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.consts.PrefConst
import io.edugma.data.base.local.*
import io.edugma.data.schedule.api.ScheduleSourcesService
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.repository.ScheduleSourcesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class ScheduleSourcesRepositoryImpl(
    private val scheduleSourcesService: ScheduleSourcesService,
    private val preferencesDS: PreferencesDS,
    private val cachedDS: CacheLocalDS
) : ScheduleSourcesRepository {
    override fun getSourceTypes() =
        scheduleSourcesService.getSourceTypes()
            .flowOn(Dispatchers.IO)

    override fun getSources(type: ScheduleSources) =
        scheduleSourcesService.getSources(type.name.lowercase())
            .flowOn(Dispatchers.IO)

    override fun getFavoriteSources(): Flow<Result<List<ScheduleSourceFull>>> =
        cachedDS.flowOf<List<ScheduleSourceFull>>(CacheConst.FavoriteScheduleSources)
            .map { it.map { it ?: emptyList() }  }
            .flowOn(Dispatchers.IO)

    override suspend fun setFavoriteSources(sources: List<ScheduleSourceFull>) {
        cachedDS.save(sources, CacheConst.FavoriteScheduleSources)
    }

    override suspend fun setSelectedSource(source: ScheduleSourceFull) {
        preferencesDS.set(source, PrefConst.SelectedScheduleSource)
    }

    override fun getSelectedSource() = preferencesDS
        .flowOf<ScheduleSourceFull>(PrefConst.SelectedScheduleSource)
        .flowOn(Dispatchers.IO)
}