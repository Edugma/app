package io.edugma.domain.account.model

import kotlinx.serialization.Serializable

@Serializable
data class Contracts (
	val contracts: Map<PaymentType, Payments>
)