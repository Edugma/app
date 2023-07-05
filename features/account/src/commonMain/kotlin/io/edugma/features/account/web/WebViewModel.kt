package io.edugma.features.account.web

import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.account.domain.repository.AuthorizationRepository

class WebViewModel(
    private val authorizationRepository: AuthorizationRepository,
) : BaseViewModel<WebState>(WebState()) {

    init {
        setTokenLoading(true)
        launchCoroutine {
            authorizationRepository.getLkToken()
                .onSuccess(::setToken)
                .onFailure { loadingError(-1) }
        }
        setWebLoading(true)
        setTokenLoading(false)
    }

    fun init(url: String, isFullScreen: Boolean) {
        mutateState {
            state = state.copy(isFullScreen = isFullScreen, url = url, errorCode = null)
        }
    }

    fun loadingEnd() {
        setWebLoading(false)
    }

    fun loadingError(code: Int) {
        mutateState {
            state = state.copy(errorCode = code, url = null)
        }
        setTokenLoading(false)
        setWebLoading(false)
    }

    private fun setTokenLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(isLoading = isLoading)
        }
    }

    private fun setWebLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(isWebViewLoading = isLoading)
        }
    }

    private fun setToken(token: String) {
        mutateState {
            state = state.copy(authToken = token)
        }
    }
}

data class WebState(
    val url: String? = null,
    val authToken: String? = null,
    val isFullScreen: Boolean = false,
    val isLoading: Boolean = false,
    val isWebViewLoading: Boolean = false,
    val errorCode: Int? = null,
)
