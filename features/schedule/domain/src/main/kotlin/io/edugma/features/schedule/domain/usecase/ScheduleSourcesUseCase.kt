package io.edugma.features.schedule.domain.usecase

import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSources
import io.edugma.features.schedule.domain.model.source.ScheduleSourcesTabs
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class ScheduleSourcesUseCase(
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {
    fun getScheduleSources() =
        flowOf(ScheduleSources.values().toList())

    fun getScheduleSources(sourcesType: ScheduleSourcesTabs): Flow<Result<List<ScheduleSourceFull>>> {
        return when (sourcesType) {
            ScheduleSourcesTabs.Favorite ->
                scheduleSourcesRepository.getFavoriteSources()
            ScheduleSourcesTabs.Complex -> error("Complex source type is not allowed")
            else -> {
                sourcesType.toSourceType()?.let {
                    scheduleSourcesRepository.getSources(it)
                } ?: flowOf(Result.success(emptyList()))
            }
        }
    }

    suspend fun setFavoriteSources(sources: List<ScheduleSourceFull>) {
        scheduleSourcesRepository.setFavoriteSources(sources)
    }

    fun getFavoriteSources() =
        scheduleSourcesRepository.getFavoriteSources()

    suspend fun addFavoriteSource(source: ScheduleSourceFull) {
        scheduleSourcesRepository.getFavoriteSources().first()
            .map { if (source in it) it else it + source }
            .onSuccess {
                scheduleSourcesRepository.setFavoriteSources(it)
            }
    }

    suspend fun deleteFavoriteSource(source: ScheduleSourceFull) {
        scheduleSourcesRepository.getFavoriteSources().first()
            .map { it - source }
            .onSuccess {
                scheduleSourcesRepository.setFavoriteSources(it)
            }
    }

    fun getSourceTypes() =
        flowOf(Result.success(ScheduleSourcesTabs.values().toList()))
    // scheduleSourcesRepository.getSourceTypes()

    fun getSources(type: ScheduleSources) =
        scheduleSourcesRepository.getSources(type)

    suspend fun setSelectedSource(source: ScheduleSourceFull) =
        scheduleSourcesRepository.setSelectedSource(source)

    fun getSelectedSource() =
        scheduleSourcesRepository.getSelectedSource()
}
