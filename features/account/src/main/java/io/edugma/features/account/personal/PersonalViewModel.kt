package io.edugma.features.account.personal

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Order
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.domain.base.utils.execute
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PersonalViewModel(private val repository: PersonalRepository) :
    BaseViewModel<PersonalState>(PersonalState()) {

        init {
            load()
        }

    fun load() {
        viewModelScope.launch {
            repository.getPersonalInfo().zip(repository.getOrders()) { data, orders ->
                when {
                    data.isSuccess && orders.isSuccess -> data.map {it to orders.getOrThrow() }
                    else -> Result.failure(data.exceptionOrNull() ?: orders.exceptionOrNull()!!)
                }
            }
                .execute(
                    onStart = { setLoading(true) },
                    onSuccess = { setData(it.first, it.second) },
                    onError = { setLoading(false) }
                )
        }
    }

    fun setLoading(loading: Boolean) {
        mutateState {
            state = state.copy(isLoading = if (!state.isPlaceholders) loading else true)
        }
    }

    fun setData(personal: Personal, orders: List<Order>) {
        mutateState { state = state.copy(
            personal = personal,
            orders = orders,
            isLoading = false,
            isPlaceholders = false
        ) }
    }
}

data class PersonalState(
    val personal: Personal? = null,
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val isPlaceholders: Boolean = true
)
