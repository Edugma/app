package io.edugma.features.account.payments

import androidx.compose.runtime.Immutable
import io.edugma.core.api.model.LceUiState
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.domain.model.payments.PaymentsDto
import io.edugma.features.account.payments.model.ContractHeaderUiModel
import io.edugma.features.account.payments.model.ContractUiModel
import io.edugma.features.account.payments.model.toUiModel

@Immutable
data class PaymentsUiState(
    val lceState: LceUiState = LceUiState.init(),
    val contractHeaders: List<ContractHeaderUiModel>? = null,
    val selectedContractHeader: ContractHeaderUiModel? = null,
    val contract: ContractUiModel? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val selectedPaymentMethod: PaymentMethod? = null,
) {
    fun toContent(data: PaymentsDto): PaymentsUiState {
        val contractHeaders = data.contracts.map { it.toUiModel() }
        return copy(
            contractHeaders = contractHeaders,
            selectedContractHeader = contractHeaders.firstOrNull { it.id == data.selected?.id },
            contract = data.selected?.toUiModel(),
        )
    }

    fun toSelectedContractHeader(id: String) = copy(
        selectedContractHeader = contractHeaders?.firstOrNull { it.id == id },
    )
}
