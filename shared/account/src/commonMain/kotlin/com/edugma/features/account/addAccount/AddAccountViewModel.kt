package com.edugma.features.account.addAccount

import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.features.account.domain.usecase.LoginAndSelectAccountUseCase

class AddAccountViewModel(
    private val loginAndSelectAccountUseCase: LoginAndSelectAccountUseCase,
) : FeatureLogic<AddAccountUiState, AddAccountAction>() {
    override fun initialState(): AddAccountUiState {
        return AddAccountUiState()
    }

    override fun processAction(action: AddAccountAction) {
        when (action) {
            AddAccountAction.OnBack -> accountRouter.back()
            is AddAccountAction.LoginEnter -> newState {
                copy(login = action.login, loginError = false)
            }
            AddAccountAction.OnLoginClick -> authorize()
            is AddAccountAction.PasswordEnter -> newState {
                copy(password = action.password, passwordError = false)
            }
            is AddAccountAction.SetPasswordVisible -> newState {
                copy(passwordVisible = action.isVisible)
            }
        }
    }

    private fun authorize() {
        launchCoroutine(
            onError = {
                setLoading(false)
                setError(it)
            }
        ) {
            val login: String = state.login
            val password: String = state.password

            if (login.isEmpty()) {
                setLoginError()
            }
            if (password.isEmpty()) {
                setPasswordError()
            }
            if (login.isEmpty() || password.isEmpty()) {
                setError("Заполните все поля")
                return@launchCoroutine
            }
            if (state.isLoading) return@launchCoroutine
            setLoading(true)
            setError(null)
            loginAndSelectAccountUseCase(
                login = login,
                password = password,
            )
            accountRouter.back()
        }
    }

    private fun setError(error: Throwable) {
        setError(error.let { "Ошибка авторизации" }) // todo добавить исключения
    }

    private fun setError(error: String?) {
        newState {
            copy(error = error.orEmpty())
        }
    }

    private fun setLoading(isLoading: Boolean) {
        newState {
            copy(isLoading = isLoading)
        }
    }

    private fun setLoginError() {
        newState {
            copy(loginError = true)
        }
    }

    private fun setPasswordError() {
        newState {
            copy(passwordError = true)
        }
    }
}
