package io.edugma.features.account.personal

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Order
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.domain.base.utils.execute
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PersonalViewModel(private val repository: PersonalRepository) :
    BaseViewModel<PersonalState>(PersonalState()) {

    init {
        load()
    }

    private fun load() {
        setLoading(true)
        val job = viewModelScope.launch {
            repository.getLocalPersonalInfo()?.let {
                setData(it)
            }
        }
        viewModelScope.launch {
            repository.getPersonalInfo()
                .onSuccess {
                    job.cancel()
                    setData(it)
                }
                .onFailure {
                    setLoading(false)
                }
                .collect()
        }
    }

    private fun setLoading(loading: Boolean) {
        mutateState {
            state = state.copy(isLoading = if (!state.isPlaceholders) loading else true)
        }
    }

    private fun setData(personal: Personal) {
        mutateState { state = state.copy(
            personal = personal,
            isLoading = false,
            isPlaceholders = false
        ) }
    }
}

data class PersonalState(
    val personal: Personal? = null,
    val isLoading: Boolean = false,
    val isPlaceholders: Boolean = true
)
