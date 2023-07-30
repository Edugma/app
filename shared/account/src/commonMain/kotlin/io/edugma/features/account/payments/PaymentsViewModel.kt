package io.edugma.features.account.payments

import io.edugma.core.api.utils.onFailure
import io.edugma.core.api.utils.onSuccess
import io.edugma.core.arch.mvi.updateState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.account.domain.model.Payments
import io.edugma.features.account.domain.repository.PaymentsRepository
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

    fun setData(data: Map<io.edugma.features.account.domain.model.PaymentType, Payments>) {
        updateState {
            copy(data = data)
        }
    }

    fun setLoading(isLoading: Boolean) {
        updateState {
            copy(
                isLoading = isLoading,
                isError = !isLoading && isError,
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
        updateState {
            copy(isError = isError, isLoading = false)
        }
    }

    fun typeChange(newIndex: Int) {
        updateState {
            copy(selectedIndex = newIndex)
        }
    }
}

data class PaymentsState(
    val data: Map<io.edugma.features.account.domain.model.PaymentType, Payments>? = null,
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
