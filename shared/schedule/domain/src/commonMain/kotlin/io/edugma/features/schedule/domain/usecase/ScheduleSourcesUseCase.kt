package io.edugma.features.schedule.domain.usecase

import io.edugma.core.api.model.PagingDto
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository

class ScheduleSourcesUseCase(
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {
    suspend fun getScheduleSources(
        type: ScheduleSourceType,
        query: String,
        page: String?,
        limit: Int,
    ): PagingDto<ScheduleSourceFull> {
        return when (type.id) {
            ScheduleSourceType.FAVORITE -> {
                PagingDto.from(
                    scheduleSourcesRepository.getFavoriteSources(),
                )
            }
            ScheduleSourceType.COMPLEX -> error("Complex source type is not allowed")
            else -> {
                scheduleSourcesRepository.getSources(
                    type = type,
                    query = query,
                    limit = limit,
                    page = page,
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
        val favoriteSources = scheduleSourcesRepository.getFavoriteSources()
        if (source in favoriteSources) {
            scheduleSourcesRepository.setFavoriteSources(favoriteSources)
        } else {
            scheduleSourcesRepository.setFavoriteSources(favoriteSources + source)
        }
    }

    suspend fun deleteFavoriteSource(id: String) {
        val favoriteSources = scheduleSourcesRepository.getFavoriteSources()
        scheduleSourcesRepository.setFavoriteSources(favoriteSources.filter { it.id != id })
    }

    // TODO скрывать избранное, если ничего не выбрано
    suspend fun getSourceTypes(): List<ScheduleSourceType> {
        val types = scheduleSourcesRepository.getSourceTypes()
        val favoriteType = ScheduleSourceType(
            id = ScheduleSourceType.FAVORITE,
            title = "Избранное",
        )
        val complexType = ScheduleSourceType(
            id = ScheduleSourceType.COMPLEX,
            title = "Продвинутый поиск",
        )
        val size = types.size + 2
        return buildList(size) {
            add(favoriteType)
            addAll(types)
        }
    }

    suspend fun setSelectedSource(source: ScheduleSourceFull) =
        scheduleSourcesRepository.setSelectedSource(source)
}
