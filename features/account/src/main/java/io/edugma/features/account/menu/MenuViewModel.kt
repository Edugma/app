package io.edugma.features.account.menu

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.usecase.AuthWithCachingDataUseCase
import io.edugma.domain.account.usecase.CurrentPayments
import io.edugma.domain.account.usecase.CurrentPerformance
import io.edugma.domain.account.usecase.DataDto
import io.edugma.domain.account.usecase.MenuDataConverterUseCase
import io.edugma.domain.account.usecase.PersonalData
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.navigation.AccountScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuViewModel(
    private val authCachingUseCase: AuthWithCachingDataUseCase,
    private val converterUseCase: MenuDataConverterUseCase,
) : BaseViewModel<MenuState>(MenuState.Loading) {
    //init
    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            if (authCachingUseCase.isAuthorized()) {
                setAuthorizedState()
                setLoading(true)
                setData(authCachingUseCase.getData())
                setLoading(false)
            } else {
                setNotAuthorizedState()
            }
        }
    }

    //auth
    fun loginInput(text: String) {
        mutateStateWithCheck<MenuState.Authorization> {
            it.copy(login = text)
        }
    }

    fun passwordInput(text: String) {
        mutateStateWithCheck<MenuState.Authorization> {
            it.copy(password = text)
        }
    }

    fun authorize() {
        viewModelScope.launch {
            lateinit var login: String
            lateinit var password: String
            (state.value as? MenuState.Authorization)
                ?.let {
                    login = it.login
                    password = it.password
                } ?: return@launch
            setLoading(true)
            authCachingUseCase.authorize(
                login = login,
                password = password,
                onAuthSuccess = ::setAuthorizedState,
                onAuthFailure = { setLoading(false) },
                onGetData = ::setData
            )
        }

    }

    //menu
    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO + NonCancellable) {
                authCachingUseCase.logout()
                setNotAuthorizedState()
            }
        }
    }

    fun openPersonal() {
        router.navigateTo(AccountScreens.Personal)
    }

    //common
    private fun setData(dataDto: DataDto) {
        mutateStateWithCheck<MenuState.Menu> { state ->
            state.copy(
                personalData = dataDto.personal?.let { converterUseCase.convert(it) },
                currentPayments = dataDto.contracts?.let { converterUseCase.convert(it) },
                currentPerformance = dataDto.performance?.let { converterUseCase.convert(it) },
            )
        }
    }

    //todo подумать как лучше
    private fun setLoading(isLoading: Boolean) {
        mutateState {
            when (state) {
                is MenuState.Authorization ->
                    mutateStateWithCheck<MenuState.Authorization> {
                        it.copy(isLoading = isLoading)
                    }
                is MenuState.Menu ->
                    mutateStateWithCheck<MenuState.Menu> {
                        it.copy(isLoading = isLoading)
                    }
                else -> {}
            }
        }
    }

    private fun setAuthorizedState() {
        mutateState {
            state = MenuState.Menu()
        }
    }

    private fun setNotAuthorizedState() {
        mutateState {
            state = MenuState.Authorization()
        }
    }

    private inline fun <reified TState : MenuState> mutateStateWithCheck(crossinline mutate: (TState) -> TState) {
        mutateState {
            val cachedState = state
            if (cachedState is TState) {
                state = mutate(cachedState)
            }
        }
    }

}

sealed class MenuState {
    object Loading : MenuState()

    data class Authorization(
        val login: String = "",
        val password: String = "",
        val savePassword: Boolean = false,
        val auth: Boolean = false,
        val isLoading: Boolean = false,
    ) : MenuState()

    data class Menu(
        val isLoading: Boolean = false,
        val webScreens: List<WebScreen> = emptyList(),
        val personalData: PersonalData? = null,
        val currentPayments: CurrentPayments? = null,
        val currentPerformance: CurrentPerformance? = null,
    ) : MenuState()
}

data class WebScreen(
    val label: String,
    val url: String
)


