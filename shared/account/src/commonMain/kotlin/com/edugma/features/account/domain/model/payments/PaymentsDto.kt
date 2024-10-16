package com.edugma.features.account.domain.model.payments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentsDto(
    @SerialName("contracts")
    val contracts: List<ContractHeader>,
    @SerialName("selected")
    val selected: Contract?,
)
