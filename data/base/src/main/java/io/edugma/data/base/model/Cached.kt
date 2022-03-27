package io.edugma.data.base.model

data class Cached<T>(
    val data: Result<T>,
    val isExpired: Boolean
)

fun <T, R> Cached<T>.map(transform: (value: T) -> R): Cached<R> {
    return Cached(
        data.map { transform(it) },
        isExpired
    )
}