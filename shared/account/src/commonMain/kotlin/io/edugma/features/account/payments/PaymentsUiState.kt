package io.edugma.features.account.payments

import androidx.compose.runtime.Immutable
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.payments.model.ContractUiModel

@Immutable
data class PaymentsUiState(
    val data: Map<String, ContractUiModel>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val selectedContract: ContractUiModel? = null,
    val selectedPaymentMethod: PaymentMethod? = null,
) {
    val isRefreshing = data.isNotNull() && isLoading && !isError
    val placeholders = data.isNull() && isLoading && !isError
    val types = data?.keys?.toList()
    val selectedType = selectedContract?.title
    val isNothingToShow
        get() = data?.isEmpty() == true && !isLoading
    val showError
        get() = isError && data.isNull()

    // TODO save order
    fun onContent(data: Map<String, ContractUiModel>) = copy(
        data = data,
        selectedContract = data.values.firstOrNull(),
    )

    fun onLoading(isLoading: Boolean) = copy(
        isLoading = isLoading,
        isError = !isLoading && isError,
    )
}

fun PaymentsUiState.getTypeByIndex(index: Int) = types?.getOrNull(index)

fun PaymentsUiState.getPaymentsByIndex(index: Int) = data?.values?.toList()?.getOrNull(index)
