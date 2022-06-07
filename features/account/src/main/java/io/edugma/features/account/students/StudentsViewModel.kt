package io.edugma.features.account.students

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Student
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.domain.base.utils.execute
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StudentsViewModel(private val repository: PeoplesRepository) :
    BaseViewModel<StudentsState>(StudentsState()) {

    var job: Job? = null

    init {
        viewModelScope.launch {
//            repository.getStudents("").execute(
//                onStart = {
//                    setLoading(true)
//                },
//                onSuccess = {
//                    setData(it.data)
//                },
//                onError = {
//                    setError(true)
//                }
//            )
        }
    }

    fun loadStudents(query: String = "") {
        job?.cancel()
        job = viewModelScope.launch {
            delay(1000)
//            repository.getStudents(query).execute(
//                onStart = {
//                    setLoading(true)
//                },
//                onSuccess = {
//                    setData(it.data)
//                },
//                onError = {
//                    setError(true)
//                }
//            )
        }
    }

    fun inputName(name: String) {
        mutateState {
            state = state.copy(query = name)
        }
        loadStudents(query = name)
    }

    fun setData(data: List<Student>) {
        mutateState {
            state = state.copy(data = data, isLoading = false, isError = false, isPlaceholders = false)
        }
    }

    fun setLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(
                isLoading = if (state.isPlaceholders) false else isLoading,
                isError = !isLoading && !state.isPlaceholders && state.isError,
                isPlaceholders = if(!isLoading) false else state.isPlaceholders )
        }
    }

    fun setError(isError: Boolean) {
        mutateState {
            state = state.copy(isError = isError, isLoading = false, isPlaceholders = false)
        }
    }

}

data class StudentsState(
    val query: String = "",
    val data: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isPlaceholders: Boolean = true
)