package io.edugma.features.account.payments

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.account.domain.model.Payments
import io.edugma.features.account.domain.repository.PaymentsRepository
import kotlinx.coroutines.async

class PaymentsViewModel(
    private val repository: PaymentsRepository,
    private val externalRouter: ExternalRouter,
) : BaseViewModel<PaymentsState>(PaymentsState()) {

    init {
        load()
    }

    fun load() {
        launchCoroutine {
            setLoading(true)
            val local = async { repository.getPaymentsLocal() }
            val remote = async { repository.getPaymentsSuspend() }
            local.await()?.contracts?.let(::setData)
            remote.await()
                .onSuccess {
                    setLoading(false)
                    setData(it.contracts)
                }
                .onFailure { setError(true) }
        }
    }

    fun setData(data: Map<io.edugma.features.account.domain.model.PaymentType, Payments>) {
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
    val data: Map<io.edugma.features.account.domain.model.PaymentType, Payments>? = null,
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
    val currentQr
        get() = if (showCurrent) selectedPayment?.qrCurrent else selectedPayment?.qrTotal
}

fun PaymentsState.getTypeByIndex(index: Int) = types?.getOrNull(index)

fun PaymentsState.getPaymentsByIndex(index: Int) = data?.values?.toList()?.getOrNull(index)
