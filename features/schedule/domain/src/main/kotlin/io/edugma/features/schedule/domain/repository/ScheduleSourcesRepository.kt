package io.edugma.features.schedule.domain.repository

import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import kotlinx.coroutines.flow.Flow

interface ScheduleSourcesRepository {
    fun getSources(type: ScheduleSources): Flow<Result<List<ScheduleSourceFull>>>

    fun getFavoriteSources(): Flow<Result<List<ScheduleSourceFull>>>
    suspend fun setFavoriteSources(sources: List<ScheduleSourceFull>)

    suspend fun setSelectedSource(source: ScheduleSourceFull?)
    fun getSelectedSource(): Flow<Result<ScheduleSourceFull?>>
    suspend fun getSelectedSourceSuspend(): ScheduleSourceFull?

    fun getSourceTypes(): Flow<Result<List<ScheduleSources>>>
}
