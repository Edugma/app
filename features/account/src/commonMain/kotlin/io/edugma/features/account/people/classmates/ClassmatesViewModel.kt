package io.edugma.features.account.people.classmates

import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.domain.account.model.student.Student
import io.edugma.features.account.domain.repository.PeoplesRepository
import io.edugma.features.account.people.common.utlis.convertAndShare
import kotlinx.coroutines.async

class ClassmatesViewModel(
    private val repository: PeoplesRepository,
    private val externalRouter: ExternalRouter,
) : BaseViewModel<ClassmatesState>(ClassmatesState()) {

    init {
        loadClassmates()
    }

    private fun loadClassmates() {
        launchCoroutine {
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
        launchCoroutine {
            setError(false)
            setLoading(true)
            repository.getClassmatesSuspend()
                .onSuccess(::setData)
                .onFailure { setError(true) }
            setLoading(false)
        }
    }

    fun onShare() {
        state.value.data?.let { students ->
            externalRouter.share(
                students.convertAndShare(),
            )
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
