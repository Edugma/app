package com.edugma.features.account.domain.model.payments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethod(
    @SerialName("type")
    val type: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("icon")
    val icon: String?,
    @SerialName("url")
    val url: String,
) {
    companion object {
        const val URL_TYPE = "url"
        const val QR_TYPE = "qr"
    }
}
