package io.edugma.domain.schedule.usecase

import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.repository.ScheduleRepository
import io.edugma.domain.schedule.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest

class ScheduleHistoryUseCase(
    private val repository: ScheduleRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository
) {
    fun getHistory() =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest {
                val source = it.getOrNull()
                if (source == null) {
                    emit(Result.failure(Exception()))
                } else {
                    emitAll(repository.getHistory(ScheduleSource(source.type, source.key)))
                }
            }
}