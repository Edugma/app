package io.edugma.domain.account.model

import io.edugma.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Order(
    val number: String,
    @Serializable(LocalDateConverter::class)
    val date: LocalDate,
    val additionalInfo: String?
) {
    companion object {
        const val TAG = "ORDERS"
    }
}
