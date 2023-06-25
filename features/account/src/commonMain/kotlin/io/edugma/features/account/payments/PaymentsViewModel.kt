package io.edugma.features.account.payments

import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.model.Payments
import io.edugma.domain.account.repository.PaymentsRepository
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.flow.collect

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
            repository.getPaymentsLocal()?.let {
                setData(it.contracts)
            }
            repository.getPayment()
                .onSuccess {
                    setData(it.contracts)
                    setLoading(false)
                }
                .onFailure {
                    setError(true)
                }
                .collect()
        }
    }

    fun setData(data: Map<PaymentType, Payments>) {
        mutateState {
            state = state.copy(data = data)
        }
    }

    fun setLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(
                isLoading = isLoading,
                isError = !isLoading && state.isError,
            )
        }
    }

    fun onOpenUri() {
        val uri = state.value.selectedType?.let {
            state.value.data?.get(it)?.qrCurrent
        }

        uri?.let {
            externalRouter.openUri(uri)
        }
    }

    fun setError(isError: Boolean) {
        mutateState {
            state = state.copy(isError = isError, isLoading = false)
        }
    }

    fun typeChange(newIndex: Int) {
        mutateState {
            state = state.copy(selectedIndex = newIndex)
        }
    }
}

data class PaymentsState(
    val data: Map<PaymentType, Payments>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val selectedIndex: Int = 0,
) {
    val isRefreshing = data.isNotNull() && isLoading && !isError
    val placeholders = data.isNull() && isLoading && !isError
    val types = data?.keys?.toList()
    val selectedType = types?.getOrNull(selectedIndex)
    val selectedPayment = data?.values?.toList()?.getOrNull(selectedIndex)
}

fun PaymentsState.getTypeByIndex(index: Int) = types?.getOrNull(index)

fun PaymentsState.getPaymentsByIndex(index: Int) = data?.values?.toList()?.getOrNull(index)
