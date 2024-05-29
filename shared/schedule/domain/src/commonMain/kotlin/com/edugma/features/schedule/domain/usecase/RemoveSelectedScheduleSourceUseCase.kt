package com.edugma.features.schedule.domain.usecase

import com.edugma.features.schedule.domain.repository.ScheduleSourcesRepository

class RemoveSelectedScheduleSourceUseCase(
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {
    suspend operator fun invoke() {
        scheduleSourcesRepository.setSelectedSource(null)
    }
}
