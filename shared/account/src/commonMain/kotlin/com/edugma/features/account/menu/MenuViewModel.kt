package com.edugma.features.account.menu

import com.edugma.core.api.utils.IO
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic2
import com.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import com.edugma.core.navigation.AccountScreens
import com.edugma.features.account.domain.model.menu.Card
import com.edugma.features.account.domain.model.menu.CardType
import com.edugma.features.account.domain.model.menu.CardType.Classmates
import com.edugma.features.account.domain.model.menu.CardType.Marks
import com.edugma.features.account.domain.model.menu.CardType.Payments
import com.edugma.features.account.domain.model.menu.CardType.Students
import com.edugma.features.account.domain.model.menu.CardType.Teachers
import com.edugma.features.account.domain.model.menu.CardType.Web
import com.edugma.features.account.domain.repository.CardsRepository
import com.edugma.features.account.domain.usecase.AuthWithCachingDataUseCase
import com.edugma.features.account.domain.usecase.CurrentPayments
import com.edugma.features.account.domain.usecase.CurrentPerformance
import com.edugma.features.account.domain.usecase.DataDto
import com.edugma.features.account.domain.usecase.LoginAndSelectAccountUseCase
import com.edugma.features.account.domain.usecase.MenuDataConverterUseCase
import com.edugma.features.account.domain.usecase.SelectedAccountUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class MenuViewModel(
    private val authCachingUseCase: AuthWithCachingDataUseCase,
    private val converterUseCase: MenuDataConverterUseCase,
    private val cardsRepository: CardsRepository,
    private val loginAndSelectAccountUseCase: LoginAndSelectAccountUseCase,
) : FeatureLogic2<MenuState>() {
    override fun initialState(): MenuState {
        return MenuState.Loading
    }

    override fun onCreate() {
        checkAuth()
    }

    private fun checkAuth() {
        launchCoroutine {
            if (authCachingUseCase.isAuthorized()) {
                setAuthorizedState()
                setLoading(true)
                startDataUpdate()
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

    fun setPasswordVisible() {
        mutateStateWithCheck<MenuState.Authorization> {
            it.copy(passwordVisible = true)
        }
    }

    fun setPasswordInvisible() {
        mutateStateWithCheck<MenuState.Authorization> {
            it.copy(passwordVisible = false)
        }
    }

    fun authorize() {
        launchCoroutine(
            onError = {
                setLoading(false)
                setError(it)
            }
        ) {
            lateinit var login: String
            lateinit var password: String
            (state as? MenuState.Authorization)
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
                    if (it.isLoading) return@launchCoroutine
                } ?: return@launchCoroutine
            setLoading(true)
            setError(null)
            loginAndSelectAccountUseCase(
                login = login,
                password = password,
            )
            startDataUpdate()
            setAuthorizedState()
        }
    }

    private fun startDataUpdate() {
        launchCoroutine {
            authCachingUseCase.getDataFlow().collect {
                setData(it)
            }
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

    fun onAccountClick() {
        accountRouter.navigateTo(AccountScreens.Accounts())
    }

    fun cardClick(type: CardType, url: String?) {
        when (type) {
            CardType.Profile -> accountRouter.navigateTo(AccountScreens.Personal())
            Students -> accountRouter.navigateTo(AccountScreens.Students())
            Teachers -> accountRouter.navigateTo(AccountScreens.Teachers())
            Classmates -> accountRouter.navigateTo(AccountScreens.Classmates())
            Payments -> accountRouter.navigateTo(AccountScreens.Payments())
            Marks -> accountRouter.navigateTo(AccountScreens.Marks())
            Web -> accountRouter.navigateTo(AccountScreens.Web(url.orEmpty()))
        }
    }

    // common
    private fun setData(dataDto: DataDto) {
        mutateStateWithCheck<MenuState.Menu> { state ->
            val selectedAccount = dataDto.selectedAccount?.let { converterUseCase.convert(it) }
                ?: dataDto.personal?.let { converterUseCase.convert(it) }
            state.copy(
                selectedAccount = selectedAccount,
                currentPayments = dataDto.contracts?.let { converterUseCase.convert(it) },
                currentPerformance = dataDto.performance?.let { converterUseCase.convert(it) },
            )
        }
    }

    // todo подумать как лучше
    private fun setLoading(isLoading: Boolean) {
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

    private fun setAuthorizedState() {
        newState {
            MenuState.Menu(cards = cardsRepository.getCards())
        }
    }

    private fun setNotAuthorizedState() {
        newState {
            MenuState.Authorization()
        }
    }

    private inline fun <reified TState : MenuState> mutateStateWithCheck(noinline mutate: (TState) -> TState) {
        newState {
            (this as? TState)?.let(mutate::invoke) ?: this
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
        val passwordVisible: Boolean = false,
    ) : MenuState()

    data class Menu(
        val isLoading: Boolean = false,
        val selectedAccount: SelectedAccountUiModel? = null,
        val currentPayments: CurrentPayments? = null,
        val currentPerformance: CurrentPerformance? = null,
        val cards: List<List<Card>> = emptyList(),
    ) : MenuState() {
        val account: AccountSelectorVO? = selectedAccount
            ?.let { AccountSelectorVO(selectedAccount.name, selectedAccount.description, selectedAccount.avatar) }
    }
}

data class WebScreen(
    val label: String,
    val url: String,
)
