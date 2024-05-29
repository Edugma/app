package com.edugma.features.account.payments.model

import androidx.compose.runtime.Immutable
import com.edugma.core.api.utils.DateFormat
import com.edugma.core.api.utils.format
import com.edugma.core.api.utils.nowLocalDate
import com.edugma.features.account.domain.model.payments.Contract
import com.edugma.features.account.domain.model.payments.Payment
import com.edugma.features.account.domain.model.payments.PaymentMethod
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

@Immutable
data class ContractUiModel(
    val id: String,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val balance: String,
    val isNegativeBalance: Boolean,
    val paymentMethods: List<PaymentMethod>,
    val payments: List<PaymentUiModel>,
)

fun Contract.toUiModel(): ContractUiModel {
    val today = Clock.System.nowLocalDate()

    val paymentsUiModelList = buildList<PaymentUiModel> {
        var previousPayment: Payment? = null
        for (payment in payments) {
            if (previousPayment == null || previousPayment.date != payment.date) {
                val dateText = if (payment.date.year == today.year) {
                    payment.date.format(DateFormat.DAY_MONTH)
                } else {
                    payment.date.format(DateFormat.FULL)
                }
                add(PaymentUiModel.Date(dateText))
            }
            add(payment.toUiModel())
            previousPayment = payment
        }
    }

    return ContractUiModel(
        id = id,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        balance = balance,
        isNegativeBalance = isNegativeBalance,
        paymentMethods = paymentMethods,
        payments = paymentsUiModelList,
    )
}
