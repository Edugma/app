package io.edugma.features.account.personal

import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.account.domain.model.Application
import io.edugma.features.account.domain.model.Personal
import io.edugma.features.account.domain.repository.ApplicationsRepository
import io.edugma.features.account.domain.repository.PersonalRepository
import kotlinx.coroutines.async

class PersonalViewModel(
    private val repository: PersonalRepository,
    private val applicationsRepository: ApplicationsRepository,
) : BaseViewModel<PersonalState>(PersonalState()) {
    init {
        load()
    }

    private fun load() {
        launchCoroutine {
            setLoading(true)
            val local = async { repository.getLocalPersonalInfo() }
            val remote = async { repository.getPersonalInfoSuspend() }
            local.await()?.also(::setData)
            remote.await()
                .onSuccess(::setData)
                .onFailure { setError(true) }
            setLoading(false)
            // не доделано api со стороны политеха
//            applicationsRepository.loadApplications()?.let { setApplications(it, true) }
        }
    }

    fun update() {
        // не доделано api со стороны политеха
//        launchCoroutine {
//            applicationsRepository.getApplications()
//                .onSuccess { setApplications(it, false) }
//                .collect()
//        }
        launchCoroutine {
            setLoading(true)
            setError(false)
            repository.getPersonalInfoSuspend()
                .onSuccess(::setData)
                .onFailure { setError(true) }
            setLoading(false)
        }
    }

    fun setColumn(column: Columns) {
        mutateState {
            state = state.copy(selectedColumn = column)
            if (column == Columns.Applications &&
                (state.applications.isNull() || state.applicationsFromCache == true)
            ) {
                loadApplications()
            }
        }
    }

    private fun loadApplications() {
//        launchCoroutine {
//            applicationsRepository.getApplications()
//                .onStart {
//                    setLoading(true)
//                }
//                .onSuccess {
//                    setApplications(it, false)
//                    setLoading(false)
//                }
//                .onFailure { setError(true) }
//                .collect()
//        }
    }

    private fun setLoading(loading: Boolean) {
        mutateState {
            state = state.copy(isLoading = loading)
        }
    }

    private fun setError(error: Boolean = true) {
        mutateState {
            state = state.copy(isError = error)
        }
    }

    private fun setData(personal: Personal) {
        mutateState {
            state = state.copy(personal = personal)
        }
    }

    private fun setApplications(applications: List<Application>, fromCache: Boolean) {
        mutateState {
            state = state.copy(applications = applications, applicationsFromCache = fromCache)
        }
    }
}

data class PersonalState(
    val personal: Personal? = null,
    val applications: List<Application>? = emptyList(),
    val applicationsFromCache: Boolean? = true,
    val selectedColumn: Columns = Columns.Orders,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    val isRefreshing = personal.isNotNull() && isLoading && !isError
    val personalPlaceholders = personal.isNull() && isLoading && !isError
    val applicationsPlaceholders = applications.isNull() && isLoading && !isError
}

enum class Columns(val label: String) {
    Orders("Приказы"), Applications("Справки")
}
