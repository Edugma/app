package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getData
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.IO
import io.edugma.core.api.utils.onSuccess
import io.edugma.data.base.consts.CacheConst.ApplicationsKey
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.applications.Application
import io.edugma.features.account.domain.repository.ApplicationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ApplicationsRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : ApplicationsRepository {

    override fun getApplications() =
        flow { emit(api.getApplications()) }
            .onSuccess(::saveApplications)
            .flowOn(Dispatchers.IO)

    override suspend fun saveApplications(applications: List<Application>) {
        cacheRepository.save(ApplicationsKey, applications)
    }

    override suspend fun loadApplications(): List<Application>? {
        return cacheRepository.getData(ApplicationsKey)
    }
}
