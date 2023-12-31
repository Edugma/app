package io.edugma.features.account.payments

import io.edugma.features.account.domain.model.payments.PaymentMethod

sealed interface PaymentsAction {
    data class OnPaymentMethodClick(val paymentMethod: PaymentMethod) : PaymentsAction
    data class OnContractSelected(val id: String) : PaymentsAction
    data object OnOpenUrl : PaymentsAction
}
