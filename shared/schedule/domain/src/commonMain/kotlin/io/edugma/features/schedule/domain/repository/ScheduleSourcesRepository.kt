package io.edugma.features.schedule.domain.repository

import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import kotlinx.coroutines.flow.Flow

interface ScheduleSourcesRepository {
    suspend fun getSources(
        type: ScheduleSources,
        query: String,
        limit: Int,
        page: String?,
    ): List<ScheduleSourceFull>

    suspend fun getFavoriteSources(): Flow<List<ScheduleSourceFull>>
    suspend fun setFavoriteSources(sources: List<ScheduleSourceFull>)

    suspend fun setSelectedSource(source: ScheduleSourceFull?)
    fun getSelectedSource(): Flow<ScheduleSourceFull?>
    suspend fun getSelectedSourceSuspend(): ScheduleSourceFull?

    fun getSourceTypes(): Flow<Result<List<ScheduleSources>>>
}
