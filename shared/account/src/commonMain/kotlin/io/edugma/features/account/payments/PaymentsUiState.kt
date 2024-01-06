package io.edugma.features.account.payments

import androidx.compose.runtime.Immutable
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.domain.model.payments.PaymentsDto
import io.edugma.features.account.payments.model.ContractHeaderUiModel
import io.edugma.features.account.payments.model.ContractUiModel
import io.edugma.features.account.payments.model.toUiModel

@Immutable
data class PaymentsUiState(
    val contractHeaders: List<ContractHeaderUiModel>? = null,
    val selectedContractHeader: ContractHeaderUiModel? = null,
    val contract: ContractUiModel? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val selectedPaymentMethod: PaymentMethod? = null,
) {
    val isRefreshing = contract.isNotNull() && isLoading && !isError
    val showPlaceholders = contract.isNull() && isLoading && !isError
    val isNothingToShow
        get() = contract == null && !isLoading
    val showError
        get() = isError && contract.isNull()

    fun toContent(data: PaymentsDto): PaymentsUiState {
        val contractHeaders = data.contracts.map { it.toUiModel() }
        return copy(
            contractHeaders = contractHeaders,
            selectedContractHeader = contractHeaders.firstOrNull { it.id == data.selected?.id },
            contract = data.selected?.toUiModel(),
        )
    }

    fun toLoading(isLoading: Boolean) = copy(
        isLoading = isLoading,
        isError = !isLoading && isError,
    )

    fun toError(isError: Boolean) = copy(
        isError = isError,
    )
}
