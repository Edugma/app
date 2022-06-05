package io.edugma.features.account.applications

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Application
import io.edugma.domain.account.repository.ApplicationsRepository
import io.edugma.domain.base.utils.execute
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.launch

class ApplicationsViewModel(private val repository: ApplicationsRepository) :
    BaseViewModelFull<ApplicationsState, ApplicationsMutator, Nothing>(ApplicationsState(), ::ApplicationsMutator) {

        init {
            loadData()
        }

    fun loadData() {
        viewModelScope.launch {
            if (!state.value.isLoading) {
                repository.getApplications().execute(
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

}

data class ApplicationsState(
    val data: List<Application?> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class ApplicationsMutator : BaseMutator<ApplicationsState>() {
    fun setData(data: List<Application>) {
        state = state.copy(data = data, isLoading = false, isError = false)
    }

    fun setLoading(isLoading: Boolean) {
        state = state.copy(isLoading = isLoading, isError = !isLoading && state.isError,
        data = if (state.data.isEmpty() && isLoading) List(5) { null } else state.data)
    }

    fun setError(isError: Boolean) {
        state = state.copy(isError = isError, isLoading = false)
    }
}