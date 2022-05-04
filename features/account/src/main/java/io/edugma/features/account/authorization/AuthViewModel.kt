package io.edugma.features.account.authorization

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.repository.AuthorizationRepository
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseActionViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authorizationRepository: AuthorizationRepository,
    private val personalRepository: PersonalRepository
    ) : BaseActionViewModel<AuthState, AuthAction>(AuthState()) {

    fun authorize() {
        viewModelScope.launch {
            authorizationRepository.authorization(state.value.login, state.value.password)
                .zip(personalRepository.getPersonalInfo()) { _, personal -> personal }
                .onStart { mutateState { state = state.copy(isLoading = true) } }
                .onSuccess {
                    loggedIn(true)
                    mutateState {
                        state = state.copy(name = it.name, avatar = it.avatar)
                    }
                }
                .onFailure {
                    loggedIn(false)
                    sendAction(AuthAction.ShowErrorMessage(it))
                }
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

    fun back() {
        router.exit()
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