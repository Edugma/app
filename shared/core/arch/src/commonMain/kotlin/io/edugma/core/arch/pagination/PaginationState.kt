package io.edugma.core.arch.pagination

import androidx.compose.runtime.Immutable

@Immutable
data class PaginationState<T>(
    val items: List<T> = emptyList(),
    // val currentPage: String? = null,
    val nextPage: String? = null,
    val pageSize: Int = 100,
    val enum: PaginationStateEnum = PaginationStateEnum.NotLoading,
) {
    fun toReset(): PaginationState<T> {
        return copy(
            items = emptyList(),
            nextPage = null,
            enum = PaginationStateEnum.Loading,
        )
    }

    fun needLoadNext(): PaginationState<T> {
        return when {
            enum == PaginationStateEnum.Loading -> this
            enum != PaginationStateEnum.End -> copy(
                enum = PaginationStateEnum.NeedLoadNext,
            )
            else -> this
        }
    }

    fun toError(): PaginationState<T> {
        return copy(
            enum = PaginationStateEnum.Error,
        )
    }

    fun toNewItems(items: List<T>, nextPage: String?): PaginationState<T> {
        val isEnded = nextPage == null || items.isEmpty()
        val enum = if (isEnded) {
            PaginationStateEnum.End
        } else {
            PaginationStateEnum.Loaded
        }

        return copy(
            items = this.items + items,
            nextPage = nextPage,
            enum = enum,
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
        fun <T> empty() = PaginationState<T>()
    }
}

enum class PaginationStateEnum {
    NotLoading, NeedLoadNext, Loading, Loaded, End, Error,
}
