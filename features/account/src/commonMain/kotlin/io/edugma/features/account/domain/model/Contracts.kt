package io.edugma.features.account.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Contracts(
    val contracts: Map<io.edugma.features.account.domain.model.PaymentType, io.edugma.features.account.domain.model.Payments>,
)
