package io.edugma.core.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagingDto<T>(
    @SerialName("count")
    val count: Int,
    @SerialName("previous")
    val previous: String? = null,
    @SerialName("next")
    val next: String? = null,
    @SerialName("data")
    val data: List<T>,
) {
    companion object {
        fun <T> empty(): PagingDto<T> {
            return PagingDto(
                count = 0,
                data = emptyList(),
                previous = null,
                next = null,
            )
        }

        fun <T> from(data: List<T>): PagingDto<T> {
            return PagingDto(
                count = data.size,
                data = data,
                previous = null,
                next = null,
            )
        }
    }
}

inline fun <T, R> PagingDto<T>.map(transform: (T) -> R): PagingDto<R> {
    return PagingDto(
        count = this.count,
        data = this.data.map(transform),
        previous = this.previous,
        next = this.next,
    )
}
