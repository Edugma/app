package com.edugma.features.app.data

import com.edugma.core.api.repository.CleanupRepository
import com.edugma.features.account.domain.repository.AuthorizationRepository
import com.edugma.features.schedule.domain.repository.ScheduleRepository
import com.edugma.features.schedule.domain.repository.ScheduleSourcesRepository

class CleanupRepositoryImpl(
    private val authorizationRepository: AuthorizationRepository,
    private val scheduleRepository: ScheduleRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) : CleanupRepository {
    override suspend fun cleanAll() {
        authorizationRepository.logout()
        scheduleRepository.clearAll()
        scheduleSourcesRepository.clearAll()
    }
}
