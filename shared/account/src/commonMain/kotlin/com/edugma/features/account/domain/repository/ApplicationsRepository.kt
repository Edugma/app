package com.edugma.features.account.domain.repository

import com.edugma.features.account.domain.model.applications.Application

interface ApplicationsRepository {
    suspend fun getApplications(): List<Application>
    suspend fun saveApplications(applications: List<Application>)
    suspend fun loadApplications(): List<Application>?
}
