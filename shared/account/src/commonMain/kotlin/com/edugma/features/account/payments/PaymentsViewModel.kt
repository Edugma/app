package com.edugma.features.account.payments

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.navigation.core.router.external.ExternalRouter
import com.edugma.core.utils.lce.launchLce
import com.edugma.features.account.domain.model.payments.PaymentMethod
import com.edugma.features.account.domain.repository.PaymentsRepository

class PaymentsViewModel(
    private val repository: PaymentsRepository,
    private val externalRouter: ExternalRouter,
) : FeatureLogic<PaymentsUiState, PaymentsAction>() {
    override fun initialState(): PaymentsUiState {
        return PaymentsUiState()
    }

    override fun onCreate() {
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
                    toContent(it.valueOrThrow)
                }
            },
        )
    }

    private fun refresh() {
        loadPayments(isRefreshing = true)
    }

    override fun processAction(action: PaymentsAction) {
        when (action) {
            PaymentsAction.OnOpenUrl -> onOpenUri()
            is PaymentsAction.OnPaymentMethodClick -> onPaymentMethodClick(action.paymentMethod)
            is PaymentsAction.OnContractSelected -> onContractSelected(action.id)
            PaymentsAction.OnRefresh -> refresh()
            PaymentsAction.OnBottomSheetClosed -> onBottomSheetClosed()
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
                showBottomSheet = true,
            )
        }
    }

    fun onBottomSheetClosed() {
        newState {
            copy(
                selectedPaymentMethod = null,
                showBottomSheet = false,
            )
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

    fun exit() {
        accountRouter.back()
    }
}
