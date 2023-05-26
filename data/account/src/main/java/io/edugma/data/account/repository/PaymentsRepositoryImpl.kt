package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.consts.CacheConst.PaymentsKey
import io.edugma.domain.account.model.Contracts
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.repository.PaymentsRepository
import io.edugma.domain.base.repository.CacheRepository
import io.edugma.domain.base.repository.get
import io.edugma.domain.base.repository.save
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PaymentsRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PaymentsRepository {

    override fun getPaymentTypes() =
        flow { emit(api.getPaymentsTypes()) }
            .flowOn(Dispatchers.IO)

    override fun getPayment(type: PaymentType?) =
        flow { emit(api.getPayments(type?.name?.lowercase().orEmpty())) }
            .onSuccess(::savePayments)
            .flowOn(Dispatchers.IO)

    override fun getPayments() = flow {
        emit(getPaymentsLocal())
    }

    override suspend fun getPaymentsSuspend(type: PaymentType?): Result<Contracts?> {
        return api.getPaymentsSuspend(type?.name?.lowercase().orEmpty())
            .onSuccess {
                withContext(Dispatchers.IO) {
                    savePayments(it)
                }
            }
    }

    override suspend fun savePayments(contracts: Contracts) {
        cacheRepository.save(PaymentsKey, contracts)
    }

    override suspend fun getPaymentsLocal(): Contracts? {
        return cacheRepository.get(PaymentsKey)
    }
}
