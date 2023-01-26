package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Contracts
import io.edugma.domain.account.model.PaymentType
import kotlinx.coroutines.flow.Flow

interface PaymentsRepository {
    fun getPaymentTypes(): Flow<Result<List<PaymentType>>>
    fun getPayment(type: PaymentType? = null): Flow<Result<Contracts>>
    fun getPayments(): Flow<Contracts?>
    suspend fun savePayments(contracts: Contracts)
    suspend fun getPaymentsLocal(): Contracts?
}
