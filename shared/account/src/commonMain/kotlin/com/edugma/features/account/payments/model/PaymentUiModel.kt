package com.edugma.features.account.payments.model

import androidx.compose.runtime.Immutable
import com.edugma.core.api.model.ListItemUiModel
import com.edugma.features.account.domain.model.payments.Payment
import kotlinx.datetime.LocalDate

@Immutable
sealed class PaymentUiModel : ListItemUiModel() {
    data class Payment(
        val id: String,
        val title: String,
        val description: String,
        val avatar: String?,
        val date: LocalDate,
        val value: String,
        val isNegative: Boolean,
        override val listContentType: Any = 0,
    ) : PaymentUiModel()

    data class Date(
        val date: String,
        override val listContentType: Any = 1,
    ) : PaymentUiModel()
}

fun Payment.toUiModel(): PaymentUiModel {
    val avatar = if (isNegative) {
        "https://img.icons8.com/fluency/48/minus.png"
    } else {
        "https://img.icons8.com/fluency/48/add--v1.png"
    }
    return PaymentUiModel.Payment(
        id = id,
        title = title,
        description = description,
        avatar = avatar,
        date = date,
        value = value,
        isNegative = isNegative,
    )
}
