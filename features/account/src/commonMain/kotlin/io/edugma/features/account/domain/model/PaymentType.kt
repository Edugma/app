package io.edugma.features.account.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentType {
    @SerialName("dormitory")
    Dormitory,

    @SerialName("education")
    Education,
}

fun io.edugma.features.account.domain.model.PaymentType.toLabel() =
    when (this) {
        io.edugma.features.account.domain.model.PaymentType.Dormitory -> "Общежитие"
        io.edugma.features.account.domain.model.PaymentType.Education -> "Обучение"
    }
