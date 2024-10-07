package com.edugma.features.account.addAccount

import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic

class AddAccountViewModel : FeatureLogic<AddAccountUiState, AddAccountAction>() {
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
        return
        launchCoroutine {
            lateinit var login: String
            lateinit var password: String
            state.let {
                login = it.login
                password = it.password
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
                if (it.isLoading) return@launchCoroutine
            }
//            setLoading(true)
//            setError(null)
//            authCachingUseCase.authorize(
//                login = login,
//                password = password,
//                onAuthSuccess = ::setAuthorizedState,
//                onAuthFailure = {
//                    setLoading(false)
//                    setError(it)
//                },
//                onGetData = ::setData,
//            )
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
