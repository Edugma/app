package io.edugma.features.schedule.domain.usecase

import io.edugma.core.api.utils.LceFlow
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository

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
