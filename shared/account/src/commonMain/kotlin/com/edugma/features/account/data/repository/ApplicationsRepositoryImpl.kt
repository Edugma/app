package com.edugma.features.account.data.repository

import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.getOnlyData
import com.edugma.core.api.repository.save
import com.edugma.data.base.consts.CacheConst.ApplicationsKey
import com.edugma.features.account.data.api.AccountService
import com.edugma.features.account.domain.model.applications.Application
import com.edugma.features.account.domain.repository.ApplicationsRepository

class ApplicationsRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : ApplicationsRepository {

    override suspend fun getApplications(): List<Application> {
        val applications = api.getApplications()
        saveApplications(applications)
        return applications
    }

    override suspend fun saveApplications(applications: List<Application>) {
        cacheRepository.save(ApplicationsKey, applications)
    }

    override suspend fun loadApplications(): List<Application>? {
        return cacheRepository.getOnlyData(ApplicationsKey)
    }
}
