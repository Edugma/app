package io.edugma.features.account.authorization

import androidx.lifecycle.viewModelScope
import io.edugma.features.base.core.mvi.BaseActionViewModel
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : BaseActionViewModel<AuthState, AuthAction>(AuthState()) {

    fun authorize() {
        viewModelScope.launch {
            mutateState {
                state = state.copy(isLoading = true)
            }
            delay(1000)
            loggedIn(true)
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
    val name: String? = "Александр",
    val avatar: String? = "https://e.mospolytech.ru/old/img/photos/upc_ea66854573a60ed7938e5ac57a32cc69_1562662162.jpg"
)

sealed class AuthAction {
    data class ShowErrorMessage(val error: Throwable) : AuthAction()
}