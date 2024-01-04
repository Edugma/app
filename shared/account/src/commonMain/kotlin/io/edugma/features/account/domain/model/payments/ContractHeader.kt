package io.edugma.features.account.domain.model.payments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContractHeader(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
)
