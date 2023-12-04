package io.edugma.features.schedule.domain.repository

import io.edugma.core.api.model.PagingDto
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import kotlinx.coroutines.flow.Flow

interface ScheduleSourcesRepository {
    suspend fun getSources(
        type: ScheduleSourceType,
        query: String,
        limit: Int,
        page: String?,
    ): PagingDto<ScheduleSourceFull>

    suspend fun getFavoriteSources(): List<ScheduleSourceFull>
    suspend fun setFavoriteSources(sources: List<ScheduleSourceFull>)

    suspend fun setSelectedSource(source: ScheduleSourceFull?)
    fun getSelectedSource(): Flow<ScheduleSourceFull?>
    suspend fun getSelectedSourceSuspend(): ScheduleSourceFull?

    suspend fun getSourceTypes(): List<ScheduleSourceType>
}
