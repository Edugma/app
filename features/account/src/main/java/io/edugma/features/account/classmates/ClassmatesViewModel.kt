package io.edugma.features.account.classmates

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.student.Student
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.domain.base.utils.execute
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import io.edugma.features.base.core.utils.isNotNull
import io.edugma.features.base.core.utils.isNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ClassmatesViewModel(private val repository: PeoplesRepository) :
    BaseViewModel<ClassmatesState>(ClassmatesState()) {

    init {
        loadClassmates()
    }

    private fun loadClassmates() {
        viewModelScope.launch {
            setLoading(true)
            repository.loadClassmates()?.let {
                setData(it)
            }
            repository.getClassmates()
                .onSuccess {
                    setData(it)
                    setLoading(false)
                }
                .onFailure { setError(true) }
                .collect()
        }
    }

    fun updateClassmates() {
        viewModelScope.launch {
            repository.getClassmates()
                .onStart { setLoading(true) }
                .onSuccess {
                    setData(it)
                    setLoading(false)
                }
                .onFailure { setError(true) }
                .collect()
        }
    }

    private fun setData(data: List<Student>) {
        mutateState {
            state = state.copy(data = data)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(isLoading = isLoading, isError = !isLoading && state.isError)
        }
    }

    private fun setError(isError: Boolean) {
        mutateState {
            state = state.copy(isError = isError, isLoading = false)
        }
    }

}

data class ClassmatesState(
    val data: List<Student>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
) {
    val placeholders = data.isNull() && isLoading && !isError
    val isRefreshing = data.isNotNull() && isLoading && !isError
}
