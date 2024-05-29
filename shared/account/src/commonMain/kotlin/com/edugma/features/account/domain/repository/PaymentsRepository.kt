package com.edugma.features.account.domain.repository

import com.edugma.core.api.utils.LceFlow
import com.edugma.features.account.domain.model.payments.PaymentsDto

interface PaymentsRepository {
    fun getPayments(
        contractId: String? = null,
        forceUpdate: Boolean = false,
    ): LceFlow<PaymentsDto>
}
