package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getData
import io.edugma.core.api.repository.save
import io.edugma.data.base.consts.CacheConst.PaymentsKey
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.payments.Contract
import io.edugma.features.account.domain.repository.PaymentsRepository

class PaymentsRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PaymentsRepository {

    override suspend fun getPayments(): List<Contract> {
        val contracts = api.getPayments()
        savePayments(contracts)
        return contracts
    }

    override suspend fun savePayments(contracts: List<Contract>) {
        cacheRepository.save(PaymentsKey, contracts)
    }

    override suspend fun getPaymentsLocal(): List<Contract>? {
        return cacheRepository.getData(PaymentsKey)
    }
}
