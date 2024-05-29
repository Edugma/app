package com.edugma.features.account.domain.repository

import com.edugma.features.account.domain.model.applications.Application
import kotlinx.coroutines.flow.Flow

interface ApplicationsRepository {
    fun getApplications(): Flow<Result<List<Application>>>
    suspend fun saveApplications(applications: List<Application>)
    suspend fun loadApplications(): List<Application>?
}
