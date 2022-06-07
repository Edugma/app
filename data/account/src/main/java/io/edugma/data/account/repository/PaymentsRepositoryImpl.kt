package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.consts.CacheConst.PaymentsKey
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.getJsonLazy
import io.edugma.data.base.local.setJsonLazy
import io.edugma.domain.account.model.Contracts
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.model.Payments
import io.edugma.domain.account.repository.PaymentsRepository
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PaymentsRepositoryImpl(
    private val api: AccountService,
    private val localStore: PreferencesDS
): PaymentsRepository {

    override fun getPaymentTypes() =
        api.getPaymentsTypes()
            .flowOn(Dispatchers.IO)

    override fun getPayment(type: PaymentType?) =
        api.getPayments(type?.name?.lowercase().orEmpty())
            .onSuccess(::savePayments)
            .flowOn(Dispatchers.IO)

    override fun getPayments() = flow {
        emit(getPaymentsLocal())
    }

    override suspend fun savePayments(contracts: Contracts) {
        localStore.setJsonLazy(contracts, PaymentsKey)
    }

    override suspend fun getPaymentsLocal(): Contracts? = localStore.getJsonLazy<Contracts>(PaymentsKey)

}