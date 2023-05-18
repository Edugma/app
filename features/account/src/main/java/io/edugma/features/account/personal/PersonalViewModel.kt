package io.edugma.features.account.personal

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Application
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.repository.ApplicationsRepository
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.utils.isNotNull
import io.edugma.features.base.core.utils.isNull
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PersonalViewModel(
    private val repository: PersonalRepository,
    private val applicationsRepository: ApplicationsRepository,
) : BaseViewModel<PersonalState>(PersonalState()) {
    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
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
//        viewModelScope.launch {
//            applicationsRepository.getApplications()
//                .onSuccess { setApplications(it, false) }
//                .collect()
//        }
        viewModelScope.launch {
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
//        viewModelScope.launch {
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
