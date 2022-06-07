package io.edugma.features.account.classmates

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Student
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.domain.base.utils.execute
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import io.edugma.features.base.core.utils.isNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ClassmatesViewModel(private val repository: PeoplesRepository) :
    BaseViewModel<ClassmatesState>(ClassmatesState()) {

    init {
        loadClassmates()
    }

    fun loadClassmates() {
        viewModelScope.launch {
            repository.getClassmates()
                .onStart { setLoading(true) }
                .onSuccess { setData(it) }
                .onFailure { setError(true) }
        }
    }

    fun setData(data: List<Student>) {
        mutateState {
            state = state.copy(data = data)
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

data class ClassmatesState(
    val data: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val placeholders: Boolean = true,
    val isError: Boolean = false
)
