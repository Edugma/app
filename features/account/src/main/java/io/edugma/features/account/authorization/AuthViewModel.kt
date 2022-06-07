package io.edugma.features.account.authorization

import android.util.Log
import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.repository.AuthorizationRepository
import io.edugma.domain.account.repository.PaymentsRepository
import io.edugma.domain.account.repository.PerformanceRepository
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.account.main.UpdateMenu
import io.edugma.features.base.core.mvi.BaseActionViewModel
import io.edugma.features.base.core.utils.isNotNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authorizationRepository: AuthorizationRepository,
    private val personalRepository: PersonalRepository,
    private val paymentsRepository: PaymentsRepository,
    private val performanceRepository: PerformanceRepository
    ) : BaseActionViewModel<AuthState, AuthAction>(AuthState()) {

    init {
        viewModelScope.launch {
            if (authorizationRepository.getSavedToken().isNotNull()) {
                personalRepository.getLocalPersonalInfo()?.let {
                    mutateState {
                        state = state.copy(name = it.name, avatar = it.avatar)
                    }
                    loggedIn(true)
                } ?: run { collectPersonalInfo() }
            }
        }
    }

    fun authorize() {
        viewModelScope.launch {
                authorizationRepository.authorization(state.value.login, state.value.password)
                    .onStart { mutateState { state = state.copy(isLoading = true) } }
                    .onSuccess {
                        collectPersonalInfo()
                        cacheData()
                    }
                    .onFailure {
                        loggedIn(false)
                        sendAction(AuthAction.ShowErrorMessage(it))
                    }
                    .collect()
            }
        }

    private fun cacheData() {
        viewModelScope.launch {
            performanceRepository.getMarksBySemester().zip(paymentsRepository.getPayment()) { _, _ ->
                viewModelScope.launch { screenResultProvider.sendEvent(UpdateMenu) }
            }.collect()
        }
    }

    private fun collectPersonalInfo() {
        viewModelScope.launch {
            personalRepository.getPersonalInfo()
                .onSuccess {
                    mutateState {
                        state = state.copy(name = it.name, avatar = it.avatar)
                    }
                    loggedIn(true)
                }
                .onFailure { exit() }
                .collect()
        }
    }

    private fun loggedIn(success: Boolean) {
        mutateState {
            state = state.copy(isLoading = false, auth = success)
        }
    }

    fun setLogin(text: String) {
        mutateState {
            state = state.copy(login = text)
        }
    }

    fun setPassword(text: String) {
        mutateState {
            state = state.copy(password = text)
        }
    }

    fun setCheckBox(isChecked: Boolean) {
        mutateState {
            state = state.copy(savePassword = isChecked)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authorizationRepository.logout()
            screenResultProvider.sendEvent(UpdateMenu)
            exit()
        }
    }

}

data class AuthState(
    val login: String = "",
    val password: String = "",
    val savePassword: Boolean = false,
    val auth: Boolean = false,
    val isLoading: Boolean = false,
    val name: String? = null,
    val avatar: String? = null
)

sealed class AuthAction {
    data class ShowErrorMessage(val error: Throwable) : AuthAction()
}