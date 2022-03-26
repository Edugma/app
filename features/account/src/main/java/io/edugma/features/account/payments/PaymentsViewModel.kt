package io.edugma.features.account.payments

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.model.Payments
import io.edugma.domain.account.repository.PaymentsRepository
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.domain.base.utils.execute
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.launch

class PaymentsViewModel(private val repository: PaymentsRepository) :
    BaseViewModelFull<PaymentsState, PaymentsMutator, Nothing>(PaymentsState(), ::PaymentsMutator) {

        init {
            load()
        }

    fun load() {
        viewModelScope.launch {
            repository.getPayments().execute(
                onStart = {
                    mutateState {
                        setLoading(true)
                    }
                },
                onSuccess = {
                    mutateState {
                        setData(it)
                    }
                },
                onError = {
                    mutateState {
                        setError(true)
                    }
                }
            )
        }
    }

}

data class PaymentsState(
    val data: List<Payments> = emptyList(),
    val types: List<PaymentType> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class PaymentsMutator : BaseMutator<PaymentsState>() {
    fun setData(data: List<Payments>) {
        state = state.copy(data = data, isLoading = false, isError = false, types = data.map { it.type!! })
    }

    fun setLoading(isLoading: Boolean) {
        state = state.copy(isLoading = isLoading, isError = !isLoading && state.isError)
    }

    fun setError(isError: Boolean) {
        state = state.copy(isError = isError, isLoading = false)
    }
}