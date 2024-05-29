package com.edugma.features.account.payments

import androidx.compose.runtime.Immutable
import com.edugma.core.api.model.LceUiState
import com.edugma.features.account.domain.model.payments.PaymentMethod
import com.edugma.features.account.domain.model.payments.PaymentsDto
import com.edugma.features.account.payments.model.ContractHeaderUiModel
import com.edugma.features.account.payments.model.ContractUiModel
import com.edugma.features.account.payments.model.toUiModel

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

    fun toContractHeaderSelected(id: String) = copy(
        selectedContractHeader = contractHeaders?.firstOrNull { it.id == id },
        contract = null,
    )
}
