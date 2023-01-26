package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.consts.CacheConst.ApplicationsKey
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.getJsonLazy
import io.edugma.data.base.local.setJsonLazy
import io.edugma.domain.account.model.Application
import io.edugma.domain.account.repository.ApplicationsRepository
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class ApplicationsRepositoryImpl(
    private val api: AccountService,
    private val localStore: PreferencesDS,
) : ApplicationsRepository {

    override fun getApplications() =
        api.getApplications()
            .onSuccess(::saveApplications)
            .flowOn(Dispatchers.IO)

    override suspend fun saveApplications(applications: List<Application>) {
        localStore.setJsonLazy(applications, ApplicationsKey)
    }

    override suspend fun loadApplications(): List<Application>? {
        return localStore.getJsonLazy(ApplicationsKey)
    }
}
