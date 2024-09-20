package com.edugma.core.arch.pagination

import androidx.compose.runtime.Immutable
import com.edugma.core.api.model.LceUiState

/**
 * @property items List of items.
 * @property nextPage Key for next page.
 * @property pageSize Size of page.
 * @property enum State of pagination.
 * @property lceState State of LCE.
 */
@Immutable
data class PaginationUiState<T>(
    val items: List<T> = emptyList(),
    // val currentPage: String? = null,
    val nextPage: String? = null,
    val pageSize: Int = 100,
    val enum: PaginationStateEnum = PaginationStateEnum.NotLoading,
    val lceState: LceUiState = LceUiState.init(),
) {
    fun toReset(): PaginationUiState<T> {
        return copy(
            items = emptyList(),
            nextPage = null,
            enum = PaginationStateEnum.Loading,
            lceState = LceUiState.init().toStartLoading(isRefresh = false),
        )
    }

    fun toRefresh(): PaginationUiState<T> {
        return copy(
            nextPage = null,
            lceState = this.lceState.toStartLoading(isRefresh = true),
        )
    }

    fun needLoadNext(): PaginationUiState<T> {
        return when {
            enum == PaginationStateEnum.Loading -> this
            enum != PaginationStateEnum.End -> copy(
                enum = PaginationStateEnum.NeedLoadNext,
            )
            else -> this
        }
    }

    fun toError(): PaginationUiState<T> {
        return copy(
            enum = PaginationStateEnum.Error,
            lceState = if (items.isEmpty()) lceState.toFinalError() else lceState,
        )
    }

    fun replaceItems(items: List<T>, nextPage: String?): PaginationUiState<T> {
        val isEnded = nextPage == null || items.isEmpty()
        val enum = if (isEnded) {
            PaginationStateEnum.End
        } else {
            PaginationStateEnum.Loaded
        }

        return copy(
            items = items,
            nextPage = nextPage,
            enum = enum,
            lceState = lceState.toFinishLoading().toContent(isEmpty = items.isEmpty()),
        )
    }

    fun addItems(items: List<T>, nextPage: String?): PaginationUiState<T> {
        val isEnded = nextPage == null || items.isEmpty()
        val enum = if (isEnded) {
            PaginationStateEnum.End
        } else {
            PaginationStateEnum.Loaded
        }

        val newItems = this.items + items

        return copy(
            items = newItems,
            nextPage = nextPage,
            enum = enum,
            lceState = lceState.toFinishLoading().toContent(isEmpty = newItems.isEmpty()),
        )
    }

    fun isContent(): Boolean {
        return enum == PaginationStateEnum.Loaded
    }

    fun isEnd(): Boolean {
        return enum == PaginationStateEnum.End
    }

    fun isError(): Boolean {
        return enum == PaginationStateEnum.Error
    }

    fun isLoadingOrNeedLoading(): Boolean {
        return enum == PaginationStateEnum.Loading ||
            enum == PaginationStateEnum.NeedLoadNext
    }

    companion object {
        fun <T> empty() = PaginationUiState<T>()
    }
}

inline fun <T, R> PaginationUiState<T>.map(transform: (T) -> R): PaginationUiState<R> {
    return PaginationUiState(
        items = this.items.map(transform),
        nextPage = this.nextPage,
        pageSize = this.pageSize,
        enum = this.enum,
        lceState = this.lceState,
    )
}

enum class PaginationStateEnum {
    NotLoading,
    NeedLoadNext,
    Loading,
    Loaded,
    End,
    Error,
}
