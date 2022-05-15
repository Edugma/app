package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.repository.PaymentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class PaymentsRepositoryImpl(
    private val api: AccountService
): PaymentsRepository {
    override fun getPaymentTypes() =
        api.getPaymentsTypes()
            .flowOn(Dispatchers.IO)

    override fun getPayment(type: PaymentType?) =
        api.getPayments(type?.name?.lowercase())
            .flowOn(Dispatchers.IO)

}