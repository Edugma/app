package io.edugma.features.account.domain.repository

import io.edugma.features.account.domain.model.Contracts
import io.edugma.features.account.domain.model.PaymentType
import kotlinx.coroutines.flow.Flow

interface PaymentsRepository {
    fun getPaymentTypes(): Flow<Result<List<io.edugma.features.account.domain.model.PaymentType>>>
    fun getPayment(type: io.edugma.features.account.domain.model.PaymentType? = null): Flow<Result<io.edugma.features.account.domain.model.Contracts>>
    fun getPayments(): Flow<io.edugma.features.account.domain.model.Contracts?>
    suspend fun getPaymentsSuspend(type: io.edugma.features.account.domain.model.PaymentType? = null): Result<io.edugma.features.account.domain.model.Contracts?>
    suspend fun savePayments(contracts: io.edugma.features.account.domain.model.Contracts)
    suspend fun getPaymentsLocal(): io.edugma.features.account.domain.model.Contracts?
}
