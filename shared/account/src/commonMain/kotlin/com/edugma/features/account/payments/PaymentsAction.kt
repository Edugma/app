package com.edugma.features.account.payments

import com.edugma.features.account.domain.model.payments.PaymentMethod

sealed interface PaymentsAction {
    data class OnPaymentMethodClick(val paymentMethod: PaymentMethod) : PaymentsAction
    data class OnContractSelected(val id: String) : PaymentsAction
    data object OnOpenUrl : PaymentsAction
    data object OnRefresh : PaymentsAction
    data object OnBottomSheetClosed : PaymentsAction
}
