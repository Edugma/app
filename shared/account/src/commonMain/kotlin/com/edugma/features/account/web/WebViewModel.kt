package com.edugma.features.account.web

import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic2
import com.edugma.features.account.domain.repository.AuthorizationRepository

class WebViewModel(
    private val authorizationRepository: AuthorizationRepository,
) : FeatureLogic2<WebState>() {
    override fun initialState(): WebState {
        return WebState()
    }

    override fun onCreate() {
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
        newState {
            copy(isFullScreen = isFullScreen, url = url, errorCode = null)
        }
    }

    fun loadingEnd() {
        setWebLoading(false)
    }

    fun loadingError(code: Int) {
        newState {
            copy(errorCode = code, url = null)
        }
        setTokenLoading(false)
        setWebLoading(false)
    }

    private fun setTokenLoading(isLoading: Boolean) {
        newState {
            copy(isLoading = isLoading)
        }
    }

    private fun setWebLoading(isLoading: Boolean) {
        newState {
            copy(isWebViewLoading = isLoading)
        }
    }

    private fun setToken(token: String) {
        newState {
            copy(authToken = token)
        }
    }

    fun exit() {
        accountRouter.back()
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
