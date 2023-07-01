package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.consts.CacheConst.ApplicationsKey
import io.edugma.domain.account.model.Application
import io.edugma.domain.account.repository.ApplicationsRepository
import io.edugma.domain.base.repository.CacheRepository
import io.edugma.domain.base.repository.get
import io.edugma.domain.base.repository.save
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
        return cacheRepository.get(ApplicationsKey)
    }
}
