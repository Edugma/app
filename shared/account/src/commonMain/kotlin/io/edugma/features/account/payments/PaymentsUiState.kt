package io.edugma.features.account.payments

import androidx.compose.runtime.Immutable
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.domain.model.payments.PaymentsApi
import io.edugma.features.account.payments.model.ContractHeaderUiModel
import io.edugma.features.account.payments.model.ContractUiModel
import io.edugma.features.account.payments.model.toUiModel

@Immutable
data class PaymentsUiState(
    val contracts: List<ContractHeaderUiModel>? = null,
    val selectedContractHeader: ContractHeaderUiModel? = null,
    val contract: ContractUiModel? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val selectedContract: ContractUiModel? = null,
    val selectedPaymentMethod: PaymentMethod? = null,
) {
    val isRefreshing = contract.isNotNull() && isLoading && !isError
    val placeholders = contract.isNull() && isLoading && !isError
    val selectedType = selectedContract
    val isNothingToShow
        get() = contract == null && !isLoading
    val showError
        get() = isError && contract.isNull()

    fun toContent(data: PaymentsApi): PaymentsUiState {
        return copy(
            contracts = data.contracts.map { it.toUiModel() },
            selectedContractHeader = null,
            contract = data.selected?.toUiModel(),
            selectedContract = contract,
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
