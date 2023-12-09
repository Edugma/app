package io.edugma.features.account.payments

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.features.account.common.LOCAL_DATA_SHOWN_ERROR
import io.edugma.features.account.domain.model.payments.Contract
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.domain.repository.PaymentsRepository
import kotlinx.coroutines.async

class PaymentsViewModel(
    private val repository: PaymentsRepository,
    private val externalRouter: ExternalRouter,
) : BaseViewModel<PaymentsState>(PaymentsState()) {

    init {
        load(isUpdate = false)
    }

    fun load(isUpdate: Boolean = true) {
        launchCoroutine {
            setLoading(true)
            val remote = async { repository.getPayments() }
            if (!isUpdate) {
                val local = async { repository.getPaymentsLocal() }
                val localContracts = local.await()
                if (localContracts != null) {
                    setData(localContracts.associateBy { it.title })
                }
            }
            runCatching { remote.await() }
                .onSuccess {
                    setLoading(false)
                    setData(it.associateBy { it.title })
                }
                .onFailure {
                    setError(true)
                    if (state.data.isNotNull()) externalRouter.showMessage(LOCAL_DATA_SHOWN_ERROR)
                }
        }
    }

    fun setData(data: Map<String, Contract>) {
        newState {
            copy(data = data)
        }
    }

    fun setLoading(isLoading: Boolean) {
        newState {
            copy(
                isLoading = isLoading,
                isError = !isLoading && isError,
            )
        }
    }

    fun onOpenUri() {
        state.selectedPaymentMethod?.url?.let { url ->
            externalRouter.openUri(url)
        }
    }

    fun onPaymentMethodClick(paymentMethod: PaymentMethod) {
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

    fun typeChange(newIndex: Int) {
        newState {
            copy(selectedIndex = newIndex)
        }
    }
}

data class PaymentsState(
    val data: Map<String, Contract>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val selectedIndex: Int = 0,
    val selectedPaymentMethod: PaymentMethod? = null,
) {
    val isRefreshing = data.isNotNull() && isLoading && !isError
    val placeholders = data.isNull() && isLoading && !isError
    val types = data?.keys?.toList()
    val selectedType = types?.getOrNull(selectedIndex)
    val selectedPayment = data?.values?.toList()?.getOrNull(selectedIndex)
    val isNothingToShow
        get() = data?.isEmpty() == true && !isLoading
    val showError
        get() = isError && data.isNull()
}

fun PaymentsState.getTypeByIndex(index: Int) = types?.getOrNull(index)

fun PaymentsState.getPaymentsByIndex(index: Int) = data?.values?.toList()?.getOrNull(index)
