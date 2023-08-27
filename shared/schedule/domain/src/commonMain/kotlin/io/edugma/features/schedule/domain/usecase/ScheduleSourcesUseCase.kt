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

    suspend fun getScheduleSources(sourcesType: ScheduleSourcesTabs): Flow<List<ScheduleSourceFull>> {
        return when (sourcesType) {
            ScheduleSourcesTabs.Favorite ->
                scheduleSourcesRepository.getFavoriteSources()
            ScheduleSourcesTabs.Complex -> error("Complex source type is not allowed")
            else -> {
                flowOf(
                    sourcesType.toSourceType()?.let {
                        scheduleSourcesRepository.getSources(it)
                    } ?: emptyList(),
                )
            }
        }
    }

    suspend fun setFavoriteSources(sources: List<ScheduleSourceFull>) {
        scheduleSourcesRepository.setFavoriteSources(sources)
    }

    suspend fun getFavoriteSources() =
        scheduleSourcesRepository.getFavoriteSources()

    suspend fun addFavoriteSource(source: ScheduleSourceFull) {
        val favoriteSources = scheduleSourcesRepository.getFavoriteSources().first()
        if (source in favoriteSources) {
            scheduleSourcesRepository.setFavoriteSources(favoriteSources)
        } else {
            scheduleSourcesRepository.setFavoriteSources(favoriteSources + source)
        }
    }

    suspend fun deleteFavoriteSource(source: ScheduleSourceFull) {
        val favoriteSources = scheduleSourcesRepository.getFavoriteSources().first()
        scheduleSourcesRepository.setFavoriteSources(favoriteSources - source)
    }

    fun getSourceTypes() =
        flowOf(Result.success(ScheduleSourcesTabs.getAllTabs()))
    // scheduleSourcesRepository.getSourceTypes()

    suspend fun getSources(type: ScheduleSources) =
        scheduleSourcesRepository.getSources(type)

    suspend fun setSelectedSource(source: ScheduleSourceFull) =
        scheduleSourcesRepository.setSelectedSource(source)

    fun getSelectedSource() =
        scheduleSourcesRepository.getSelectedSource()
}
