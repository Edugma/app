package com.edugma.features.schedule.domain.usecase

import com.edugma.core.api.utils.LceFlow
import com.edugma.features.schedule.domain.model.compact.CompactSchedule
import com.edugma.features.schedule.domain.model.source.ScheduleSource
import com.edugma.features.schedule.domain.repository.ScheduleRepository
import com.edugma.features.schedule.domain.repository.ScheduleSourcesRepository

class GetCurrentScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {
    suspend operator fun invoke(forceUpdate: Boolean = false): LceFlow<CompactSchedule> {
        val source = scheduleSourcesRepository.getSelectedSourceSuspend() ?: return LceFlow.empty()

        return scheduleRepository.getRawSchedule(
            ScheduleSource(source.type, source.id),
            forceUpdate = forceUpdate,
        )
    }
}
