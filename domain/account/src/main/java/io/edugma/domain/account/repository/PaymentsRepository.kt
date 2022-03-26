package io.edugma.domain.account.repository

import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.model.Payments
import kotlinx.coroutines.flow.Flow

interface PaymentsRepository {
    fun getPaymentTypes(): Flow<Result<List<PaymentType>>>
    fun getPayment(type: PaymentType): Flow<Result<Payments>>
    fun getPayments(): Flow<Result<List<Payments>>>
}