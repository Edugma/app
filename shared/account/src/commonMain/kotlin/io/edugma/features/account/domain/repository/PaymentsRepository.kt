package io.edugma.features.account.domain.repository

import io.edugma.features.account.domain.model.Contracts
import io.edugma.features.account.domain.model.PaymentType
import kotlinx.coroutines.flow.Flow

interface PaymentsRepository {
    fun getPaymentTypes(): Flow<Result<List<PaymentType>>>
    fun getPayment(type: PaymentType? = null): Flow<Result<Contracts>>
    fun getPayments(): Flow<Contracts?>
    suspend fun getPaymentsSuspend(type: PaymentType? = null): Result<Contracts>
    suspend fun savePayments(contracts: Contracts)
    suspend fun getPaymentsLocal(): Contracts?
}
