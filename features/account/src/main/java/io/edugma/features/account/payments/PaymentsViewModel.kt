package io.edugma.features.account.payments

import android.util.Log
import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.model.Payments
import io.edugma.domain.account.repository.PaymentsRepository
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.domain.base.utils.execute
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.launch

class PaymentsViewModel(private val repository: PaymentsRepository) :
    BaseViewModel<PaymentsState>(PaymentsState()) {

        init {
            load()
        }

    fun load() {
        viewModelScope.launch {
            repository.getPayment().execute(
                onStart = {
                    mutateState {
                        setLoading(true)
                    }
                },
                onSuccess = {
                    mutateState {
                        setData(it.contracts)
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

    fun setData(data: Map<PaymentType, Payments>) {
        mutateState {
            state = state.copy(data = data.values.toList(), isLoading = false, isError = false, types = data.keys.toList())
        }
    }

    fun setLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(isLoading = isLoading, isError = !isLoading && state.isError)
        }
    }

    fun setError(isError: Boolean) {
        mutateState {
            state = state.copy(isError = isError, isLoading = false)
        }
    }

}

data class PaymentsState(
    val data: List<Payments> = emptyList(),
    val types: List<PaymentType> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)