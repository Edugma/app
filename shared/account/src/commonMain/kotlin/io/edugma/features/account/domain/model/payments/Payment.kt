package io.edugma.features.account.domain.model.payments

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("date")
    val date: LocalDate,
    @SerialName("value")
    val value: String,
    @SerialName("isNegative")
    val isNegative: Boolean,
)
