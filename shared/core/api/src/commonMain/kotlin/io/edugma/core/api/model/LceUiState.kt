package io.edugma.core.api.model

import androidx.compose.runtime.Immutable

@Immutable
data class LceUiState(
    val content: Content?,
    val loading: Loading?,
    val error: Error?,
) {
    val isRefreshing: Boolean
        get() = loading != null && loading.isRefresh

    val showContent: Boolean
        get() = content != null && content.isEmpty.not()

    val showEmptyContent: Boolean
        get() = content != null && content.isEmpty && loading == null

    val showPlaceholder: Boolean
        get() = content == null && loading != null && error == null

    val showError: Boolean
        get() = content == null && loading == null && error != null

    fun toContent(isEmpty: Boolean): LceUiState {
        return copy(
            content = Content(isEmpty = isEmpty),
            error = null,
        )
    }

    fun toLoading(isLoading: Boolean, isRefresh: Boolean = false): LceUiState {
        return copy(
            loading = if (isLoading) Loading(isRefresh = isRefresh) else null,
        )
    }

    fun toStartLoading(isRefresh: Boolean): LceUiState {
        return copy(
            content = if (isRefresh) content else null,
            loading = Loading(isRefresh = isRefresh),
        )
    }

    fun toFinishLoading(): LceUiState {
        return copy(
            loading = null,
        )
    }

    fun toError(): LceUiState {
        return copy(
            error = Error(),
        )
    }

    fun toFinalError(): LceUiState {
        return copy(
            loading = null,
            error = Error(),
        )
    }

    @Immutable
    data class Content(
        val isEmpty: Boolean,
    )

    @Immutable
    data class Loading(
        val isRefresh: Boolean,
    )

    @Immutable
    data class Error(
        val type: Int = 0,
    )

    companion object {
        fun init(): LceUiState {
            return LceUiState(
                content = null,
                loading = null,
                error = null,
            )
        }
    }
}
