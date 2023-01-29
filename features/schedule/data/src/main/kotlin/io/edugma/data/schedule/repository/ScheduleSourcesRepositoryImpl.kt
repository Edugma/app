package io.edugma.data.schedule.repository

import android.util.Log
import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.consts.PrefConst
import io.edugma.data.base.local.CacheLocalDS
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.flowOf
import io.edugma.data.base.local.get
import io.edugma.data.base.local.set
import io.edugma.data.schedule.api.ScheduleSourcesService
import io.edugma.domain.base.utils.onFailure
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ScheduleSourcesRepositoryImpl(
    private val scheduleSourcesService: ScheduleSourcesService,
    private val preferencesDS: PreferencesDS,
    private val cachedDS: CacheLocalDS,
) : ScheduleSourcesRepository {
    private val TAG = "ScheduleSourcesReposito"

    override fun getSourceTypes() =
        scheduleSourcesService.getSourceTypes()
            .flowOn(Dispatchers.IO)

    override fun getSources(type: ScheduleSources) =
        scheduleSourcesService.getSources(type.name.lowercase())
            .onFailure { Log.e(TAG, "getSources: ", it) }
            .flowOn(Dispatchers.IO)

    override fun getFavoriteSources(): Flow<Result<List<ScheduleSourceFull>>> =
        cachedDS.flowOf<List<ScheduleSourceFull>>(CacheConst.FavoriteScheduleSources)
            .map { it.map { it ?: emptyList() } }
            .flowOn(Dispatchers.IO)

    override suspend fun setFavoriteSources(sources: List<ScheduleSourceFull>) {
        cachedDS.save(sources, CacheConst.FavoriteScheduleSources)
    }

    override suspend fun setSelectedSource(source: ScheduleSourceFull?) {
        preferencesDS.set(source, PrefConst.SelectedScheduleSource)
    }

    override fun getSelectedSource() = preferencesDS
        .flowOf<ScheduleSourceFull>(PrefConst.SelectedScheduleSource)
        .flowOn(Dispatchers.IO)

    override suspend fun getSelectedSourceSuspend() = withContext(Dispatchers.IO) {
        preferencesDS.get<ScheduleSourceFull?>(PrefConst.SelectedScheduleSource, null)
    }
}
