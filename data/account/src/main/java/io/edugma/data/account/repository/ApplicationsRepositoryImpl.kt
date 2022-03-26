package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.domain.account.repository.ApplicationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class ApplicationsRepositoryImpl(
    private val api: AccountService
): ApplicationsRepository {
    override fun getApplications() =
        api.getApplications()
            .flowOn(Dispatchers.IO)
}