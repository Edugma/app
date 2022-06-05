package io.edugma.features.account.teachers

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.domain.base.utils.execute
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.launch

class TeachersViewModel(private val repository: PeoplesRepository) :
    BaseViewModelFull<TeachersState, TeachersMutator, Nothing>(TeachersState(), ::TeachersMutator) {
        init {
            load()
        }

    fun load() {
        viewModelScope.launch {
            repository.getTeachers("ФИО").execute(
                onStart = {
                    mutateState {
                        setLoading(true)
                    }
                },
                onSuccess = {
                    mutateState {
                        setData(it.data)
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

data class TeachersState(
    val data: List<Teacher> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class TeachersMutator : BaseMutator<TeachersState>() {
    fun setData(data: List<Teacher>) {
        state = state.copy(data = data, isLoading = false, isError = false)
    }

    fun setLoading(isLoading: Boolean) {
        state = state.copy(isLoading = isLoading, isError = !isLoading && state.isError)
    }

    fun setError(isError: Boolean) {
        state = state.copy(isError = isError, isLoading = false)
    }
}