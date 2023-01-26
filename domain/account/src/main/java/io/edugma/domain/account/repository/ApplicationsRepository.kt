package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Application
import kotlinx.coroutines.flow.Flow

interface ApplicationsRepository {
    fun getApplications(): Flow<Result<List<Application>>>
    suspend fun saveApplications(applications: List<Application>)
    suspend fun loadApplications(): List<Application>?
}
