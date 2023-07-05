package io.edugma.features.account.domain.repository

import io.edugma.features.account.domain.model.Application
import kotlinx.coroutines.flow.Flow

interface ApplicationsRepository {
    fun getApplications(): Flow<Result<List<io.edugma.features.account.domain.model.Application>>>
    suspend fun saveApplications(applications: List<io.edugma.features.account.domain.model.Application>)
    suspend fun loadApplications(): List<io.edugma.features.account.domain.model.Application>?
}
