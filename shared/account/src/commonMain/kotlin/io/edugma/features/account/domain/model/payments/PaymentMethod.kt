package io.edugma.features.account.domain.model.payments

import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethod(
    val type: String,
    val title: String,
    val description: String,
    val url: String,
) {
    companion object {
        const val URL_TYPE = "url"
    }
}
