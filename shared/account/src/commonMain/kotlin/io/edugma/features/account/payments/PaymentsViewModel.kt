package io.edugma.features.account.payments

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.isNotNull
import io.edugma.features.account.common.LOCAL_DATA_SHOWN_ERROR
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.domain.repository.PaymentsRepository
import io.edugma.features.account.payments.model.toUiModel
import kotlinx.coroutines.async

class PaymentsViewModel(
    private val repository: PaymentsRepository,
    private val externalRouter: ExternalRouter,
) : BaseActionViewModel<PaymentsUiState, PaymentsAction>(PaymentsUiState()) {

    init {
        load(isUpdate = false)
    }

    fun load(isUpdate: Boolean = true) {
        launchCoroutine {
            newState {
                onLoading(true)
            }
            val remote = async { repository.getPayments().map { it.toUiModel() } }
            if (!isUpdate) {
                val local = async { repository.getPaymentsLocal()?.map { it.toUiModel() } }
                val localContracts = local.await()
                if (localContracts != null) {
                    newState {
                        onContent(localContracts.associateBy { it.title })
                    }
                }
            }
            runCatching { remote.await() }
                .onSuccess {
                    newState {
                        onLoading(false)
                            .onContent(it.associateBy { it.title })
                    }
                }
                .onFailure {
                    setError(true)
                    if (state.data.isNotNull()) externalRouter.showMessage(LOCAL_DATA_SHOWN_ERROR)
                }
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
        newState {
            copy(selectedContract = data?.values?.firstOrNull { it.id == id })
        }
    }
}
