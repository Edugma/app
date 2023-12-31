package io.edugma.features.account.domain.model.payments

import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethod(
    val type: String,
    val title: String,
    val description: String,
    // TODO remove
    val icon: String = "https://img.icons8.com/fluency/96/qr-code.png",
    val url: String,
) {
    companion object {
        const val URL_TYPE = "url"
        const val QR_TYPE = "qr"
    }
}
