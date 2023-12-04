package io.edugma.features.account.payments

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.features.account.common.LOCAL_DATA_SHOWN_ERROR
import io.edugma.features.account.domain.model.payments.PaymentType
import io.edugma.features.account.domain.model.payments.Payments
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
            val remote = async { repository.getPayments(type = null) }
            if (!isUpdate) {
                val local = async { repository.getPaymentsLocal() }
                local.await()?.contracts?.let(::setData)
            }
            remote.await()
                .onSuccess {
                    setLoading(false)
                    setData(it.contracts)
                }
                .onFailure {
                    setError(true)
                    if (state.data.isNotNull()) externalRouter.showMessage(LOCAL_DATA_SHOWN_ERROR)
                }
        }
    }

    fun setData(data: Map<PaymentType, Payments>) {
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
        val uri = stateFlow.value.selectedType?.let {
            stateFlow.value.data?.get(it)?.qrCurrent
        }

        uri?.let {
            externalRouter.openUri(uri)
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

    fun showCurrentQr() {
        newState {
            copy(showCurrent = true)
        }
    }

    fun showTotalQr() {
        newState {
            copy(showCurrent = false)
        }
    }
}

data class PaymentsState(
    val data: Map<PaymentType, Payments>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val showCurrent: Boolean = true,
    val selectedIndex: Int = 0,
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
    val currentQr
        get() = if (showCurrent) selectedPayment?.qrCurrent else selectedPayment?.qrTotal
}

fun PaymentsState.getTypeByIndex(index: Int) = types?.getOrNull(index)

fun PaymentsState.getPaymentsByIndex(index: Int) = data?.values?.toList()?.getOrNull(index)
