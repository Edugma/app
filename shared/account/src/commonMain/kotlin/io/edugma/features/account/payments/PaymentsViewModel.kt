package io.edugma.features.account.payments

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.lce.launchLce
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.domain.repository.PaymentsRepository

class PaymentsViewModel(
    private val repository: PaymentsRepository,
    private val externalRouter: ExternalRouter,
) : BaseActionViewModel<PaymentsUiState, PaymentsAction>(PaymentsUiState()) {

    init {
        loadPayments(isRefreshing = false)
    }

    private fun loadPayments(isRefreshing: Boolean) {
        launchLce(
            lceProvider = {
                repository.getPayments(
                    contractId = state.selectedContractHeader?.id,
                    forceUpdate = isRefreshing,
                )
            },
            getLceState = state::lceState,
            setLceState = { newState { copy(lceState = it) } },
            isContentEmpty = { state.contract == null },
            isRefreshing = isRefreshing,
            onSuccess = {
                newState {
                    toContent(it.value)
                }
            },
        )
    }

    private fun refresh() {
        loadPayments(isRefreshing = true)
    }

    override fun onAction(action: PaymentsAction) {
        when (action) {
            PaymentsAction.OnOpenUrl -> onOpenUri()
            is PaymentsAction.OnPaymentMethodClick -> onPaymentMethodClick(action.paymentMethod)
            is PaymentsAction.OnContractSelected -> onContractSelected(action.id)
            PaymentsAction.OnRefresh -> refresh()
        }
    }

    private fun onOpenUri() {
        state.selectedPaymentMethod?.url?.let { url ->
            externalRouter.openUri(url)
        }
    }

    private fun onPaymentMethodClick(paymentMethod: PaymentMethod) {
        newState {
            copy(
                selectedPaymentMethod = paymentMethod,
            )
        }
    }

    fun onBottomSheetClosed() {
        newState {
            copy(selectedPaymentMethod = null)
        }
    }

    private fun onContractSelected(id: String) {
        if (state.selectedContractHeader?.id != id) {
            newState {
                toContractHeaderSelected(id)
            }
            loadPayments(isRefreshing = false)
        }
    }
}
