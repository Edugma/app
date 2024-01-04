package io.edugma.features.account.domain.repository

import io.edugma.core.api.utils.LceFlow
import io.edugma.features.account.domain.model.payments.PaymentsApi

interface PaymentsRepository {
    suspend fun getPayments(
        contractId: String? = null,
        forceUpdate: Boolean = false,
    ): LceFlow<PaymentsApi>
}
