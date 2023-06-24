package io.edugma.domain.account.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentType {
    @SerialName("dormitory")
    Dormitory,

    @SerialName("education")
    Education,
}

fun PaymentType.toLabel() =
    when (this) {
        PaymentType.Dormitory -> "Общежитие"
        PaymentType.Education -> "Обучение"
    }
