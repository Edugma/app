package com.edugma.features.schedule.domain.usecase

import com.edugma.features.schedule.domain.repository.ScheduleSourcesRepository

class IsScheduleSourceSelectedUseCase(
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {
    suspend operator fun invoke(): Boolean {
        return scheduleSourcesRepository.getSelectedSourceSuspend() != null
    }
}
