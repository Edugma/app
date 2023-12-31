package io.edugma.features.account.domain.model.payments

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Contract(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("startDate")
    val startDate: LocalDate,
    @SerialName("endDate")
    val endDate: LocalDate,
    @SerialName("balance")
    val balance: String,
    @SerialName("isNegativeBalance")
    val isNegativeBalance: Boolean,
    @SerialName("paymentMethods")
    val paymentMethods: List<PaymentMethod>,
    @SerialName("payments")
    val payments: List<Payment>,
)
