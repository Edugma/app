package io.edugma.features.account.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Payments(
    val id: String,
    val student: String,
    val number: String,
    val name: String,
    val type: String,
    val level: String? = null,
    val dormNum: String? = null,
    val dormRoom: String? = null,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val qrCurrent: String,
    val qrTotal: String,
    val sum: String,
    val balance: String,
    val balanceCurrent: String,
    val lastPaymentDate: LocalDate? = null,
    val payments: List<io.edugma.features.account.domain.model.Payment>,
)
