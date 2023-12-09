package io.edugma.features.account.domain.repository

import io.edugma.features.account.domain.model.payments.Contract

interface PaymentsRepository {
    suspend fun getPayments(): List<Contract>
    suspend fun savePayments(contracts: List<Contract>)
    suspend fun getPaymentsLocal(): List<Contract>?
}
