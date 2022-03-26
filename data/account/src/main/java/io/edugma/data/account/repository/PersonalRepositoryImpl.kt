package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.domain.account.repository.PersonalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class PersonalRepositoryImpl(
    private val api: AccountService
): PersonalRepository {
    override fun getPersonalInfo() =
        api.getPersonalInfo()
            .flowOn(Dispatchers.IO)

    override fun getOrders() =
        api.getOrders()
            .flowOn(Dispatchers.IO)
}