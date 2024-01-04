package io.edugma.features.account.payments

import io.edugma.core.api.utils.onResult
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.features.account.common.LOCAL_DATA_SHOWN_ERROR
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.domain.repository.PaymentsRepository

class PaymentsViewModel(
    private val repository: PaymentsRepository,
    private val externalRouter: ExternalRouter,
) : BaseActionViewModel<PaymentsUiState, PaymentsAction>(PaymentsUiState()) {

    init {
        load(forceUpdate = false)
    }

    fun load(forceUpdate: Boolean = true, contractId: String? = null) {
        launchCoroutine {
            newState {
                toLoading(true)
            }
            repository.getPayments(contractId = contractId, forceUpdate = forceUpdate).onResult(
                onSuccess = {
                    newState {
                        toContent(it.value)
                            .toLoading(it.isLoading)
                    }
                },
                onFailure = {
                    newState {
                        // TODO показывать ошибку на весь экран только если isLoading = false
                        toError(true)
                            .toLoading(false)
                    }
                    if (it.isLoading.not()) {
                        externalRouter.showMessage(LOCAL_DATA_SHOWN_ERROR)
                    }
                },
            )
        }
    }

    override fun onAction(action: PaymentsAction) {
        when (action) {
            PaymentsAction.OnOpenUrl -> onOpenUri()
            is PaymentsAction.OnPaymentMethodClick -> onPaymentMethodClick(action.paymentMethod)
            is PaymentsAction.OnContractSelected -> onContractSelected(action.id)
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

    private fun setError(isError: Boolean) {
        newState {
            copy(isError = isError, isLoading = false)
        }
    }

    private fun onContractSelected(id: String) {
        load(contractId = id)
    }
}
