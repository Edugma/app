package io.edugma.features.account.payments

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.model.Payments
import io.edugma.domain.account.repository.PaymentsRepository
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.utils.isNotNull
import io.edugma.features.base.core.utils.isNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PaymentsViewModel(private val repository: PaymentsRepository) :
    BaseViewModel<PaymentsState>(PaymentsState()) {

        init {
            load()
        }

    fun load() {
        viewModelScope.launch {
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
                isError = !isLoading && state.isError
            )
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