package io.edugma.features.account.people.classmates

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.core.router.external.ExternalRouter
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.account.common.LOCAL_DATA_SHOWN_ERROR
import io.edugma.features.account.domain.model.student.Student
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
                    if (state.data.isNotNull()) externalRouter.showMessage(LOCAL_DATA_SHOWN_ERROR)
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
                .onFailure {
                    setError(true)
                    externalRouter.showMessage(LOCAL_DATA_SHOWN_ERROR)
                }
            setLoading(false)
        }
    }

    fun onShare() {
        stateFlow.value.data?.let { students ->
            externalRouter.share(
                students.convertAndShare(),
            )
        }
    }

    private fun setData(data: List<Student>) {
        newState {
            copy(data = data)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        newState {
            copy(isLoading = isLoading)
        }
    }

    private fun setError(isError: Boolean) {
        newState {
            copy(isError = isError)
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
    val showError = data.isNull() && isError
}
