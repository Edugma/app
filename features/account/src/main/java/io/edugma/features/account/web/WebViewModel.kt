package io.edugma.features.account.web

import io.edugma.features.base.core.mvi.BaseViewModel

class WebViewModel : BaseViewModel<WebState>(WebState()) {

    fun init(url: String, isFullScreen: Boolean) {
        //todo добавить проброс токена авторизации лк с бэка (можно отдельной ручкой)
        mutateState {
            state = state.copy(isFullScreen = isFullScreen, url = url)
        }
        setLoading(true)
    }

    fun loadingEnd() {
        setLoading(false)
    }

    fun loadingError(code: Int) {
        mutateState {
            state = state.copy(errorCode = code, url = null)
        }
        setLoading(false)
    }

    private fun setLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(isLoading = isLoading)
        }
    }

}

data class WebState(
    val url: String? = null,
    val authToken: String? = null,
    val isFullScreen: Boolean = false,
    val isLoading: Boolean = false,
    val errorCode: Int? = null,
)
