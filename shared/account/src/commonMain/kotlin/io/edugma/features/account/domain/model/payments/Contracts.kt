package io.edugma.features.account.domain.model.payments

import kotlinx.serialization.Serializable

@Serializable
data class Contracts(
    val contracts: Map<PaymentType, Payments>,
)