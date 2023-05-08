package io.edugma.features.account.people.classmates

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.student.Student
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.utils.isNotNull
import io.edugma.features.base.core.utils.isNull
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ClassmatesViewModel(private val repository: PeoplesRepository) :
    BaseViewModel<ClassmatesState>(ClassmatesState()) {

    init {
        loadClassmates()
    }

    private fun loadClassmates() {
        viewModelScope.launch {
            setLoading(true)

            val localData = async { repository.loadClassmates() }
            val remoteData = async { repository.getClassmatesSuspend() }

            localData.await()?.let(::setData)
            remoteData.await()
                .onSuccess(::setData)
                .onFailure {
                    setError(true)
                }
            setLoading(false)
        }
    }

    fun updateClassmates() {
        viewModelScope.launch {
            setError(false)
            setLoading(true)
            repository.getClassmatesSuspend()
                .onSuccess(::setData)
                .onFailure { setError(true) }
            setLoading(false)
        }
    }

    private fun setData(data: List<Student>) {
        mutateState {
            state = state.copy(data = data)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(isLoading = isLoading)
        }
    }

    private fun setError(isError: Boolean) {
        mutateState {
            state = state.copy(isError = isError)
        }
    }
}

data class ClassmatesState(
    val data: List<Student>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    val placeholders = data.isNull() && isLoading && !isError
    val isRefreshing = data.isNotNull() && isLoading && !isError
}
