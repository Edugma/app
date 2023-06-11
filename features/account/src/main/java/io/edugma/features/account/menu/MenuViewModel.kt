package io.edugma.features.account.menu

import io.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import io.edugma.core.navigation.AccountScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.domain.account.model.menu.Card
import io.edugma.domain.account.model.menu.CardType
import io.edugma.domain.account.model.menu.CardType.Classmates
import io.edugma.domain.account.model.menu.CardType.Marks
import io.edugma.domain.account.model.menu.CardType.Payments
import io.edugma.domain.account.model.menu.CardType.Students
import io.edugma.domain.account.model.menu.CardType.Teachers
import io.edugma.domain.account.model.menu.CardType.Web
import io.edugma.domain.account.repository.CardsRepository
import io.edugma.domain.account.usecase.AuthWithCachingDataUseCase
import io.edugma.domain.account.usecase.CurrentPayments
import io.edugma.domain.account.usecase.CurrentPerformance
import io.edugma.domain.account.usecase.DataDto
import io.edugma.domain.account.usecase.MenuDataConverterUseCase
import io.edugma.domain.account.usecase.PersonalData
import io.edugma.features.base.core.mvi.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class MenuViewModel(
    private val authCachingUseCase: AuthWithCachingDataUseCase,
    private val converterUseCase: MenuDataConverterUseCase,
    private val cardsRepository: CardsRepository,
) : BaseViewModel<MenuState>(MenuState.Loading) {
    // init
    init {
        checkAuth()
    }

    private fun checkAuth() {
        launchCoroutine {
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

    // auth
    fun loginInput(text: String) {
        mutateStateWithCheck<MenuState.Authorization> {
            it.copy(login = text, loginError = false)
        }
    }

    fun passwordInput(text: String) {
        mutateStateWithCheck<MenuState.Authorization> {
            it.copy(password = text, passwordError = false)
        }
    }

    fun authorize() {
        launchCoroutine {
            lateinit var login: String
            lateinit var password: String
            (state.value as? MenuState.Authorization)
                ?.let {
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
                } ?: return@launchCoroutine
            setLoading(true)
            setError(null)
            authCachingUseCase.authorize(
                login = login,
                password = password,
                onAuthSuccess = ::setAuthorizedState,
                onAuthFailure = {
                    setLoading(false)
                    setError(it)
                },
                onGetData = ::setData,
            )
        }
    }

    private fun setError(error: Throwable) {
        setError(error.let { "Ошибка авторизации" }) // todo добавить исключения
    }

    private fun setError(error: String?) {
        mutateStateWithCheck<MenuState.Authorization> {
            it.copy(error = error.orEmpty())
        }
    }

    private fun setLoginError() {
        mutateStateWithCheck<MenuState.Authorization> {
            it.copy(loginError = true)
        }
    }

    private fun setPasswordError() {
        mutateStateWithCheck<MenuState.Authorization> {
            it.copy(passwordError = true)
        }
    }

    // menu
    fun logout() {
        launchCoroutine {
            withContext(Dispatchers.IO + NonCancellable) {
                authCachingUseCase.logout()
                setNotAuthorizedState()
            }
        }
    }

    fun openPersonal() {
        router.navigateTo(AccountScreens.Personal())
    }

    fun cardClick(type: CardType, url: String?) {
        when (type) {
            Students -> router.navigateTo(AccountScreens.Students())
            Teachers -> router.navigateTo(AccountScreens.Teachers())
            Classmates -> router.navigateTo(AccountScreens.Classmates())
            Payments -> router.navigateTo(AccountScreens.Payments())
            Marks -> router.navigateTo(AccountScreens.Marks())
            Web -> router.navigateTo(AccountScreens.Web(url.orEmpty()))
        }
    }

    // common
    private fun setData(dataDto: DataDto) {
        mutateStateWithCheck<MenuState.Menu> { state ->
            state.copy(
                personalData = dataDto.personal?.let { converterUseCase.convert(it) },
                currentPayments = dataDto.contracts?.let { converterUseCase.convert(it) },
                currentPerformance = dataDto.performance?.let { converterUseCase.convert(it) },
            )
        }
    }

    // todo подумать как лучше
    private fun setLoading(isLoading: Boolean) {
        when (state.value) {
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

    private fun setAuthorizedState() {
        mutateState {
            state = MenuState.Menu(cards = cardsRepository.getCards())
        }
    }

    private fun setNotAuthorizedState() {
        mutateState {
            state = MenuState.Authorization()
        }
    }

    private inline fun <reified TState : MenuState> mutateStateWithCheck(noinline mutate: (TState) -> TState) {
        mutateState {
            state = (state as? TState)?.let(mutate::invoke) ?: state
        }
    }
}

sealed class MenuState {
    object Loading : MenuState()

    data class Authorization(
        val login: String = "",
        val loginError: Boolean = false,
        val password: String = "",
        val passwordError: Boolean = false,
        val error: String = "",
        val isLoading: Boolean = false,
    ) : MenuState()

    data class Menu(
        val isLoading: Boolean = false,
        val personalData: PersonalData? = null,
        val currentPayments: CurrentPayments? = null,
        val currentPerformance: CurrentPerformance? = null,
        val cards: List<List<Card>> = emptyList(),
    ) : MenuState() {
        val account: AccountSelectorVO? = personalData
            ?.let { AccountSelectorVO(personalData.fullName, personalData.label, personalData.avatar) }
    }
}

data class WebScreen(
    val label: String,
    val url: String,
)
