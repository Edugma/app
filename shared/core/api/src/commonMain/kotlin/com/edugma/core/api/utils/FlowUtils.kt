package com.edugma.core.api.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

private const val StopTimeoutMillis: Long = 5000

/**
 * A [SharingStarted] meant to be used with a [StateFlow] to expose data to a view.
 *
 * When the view stops observing, upstream flows stay active for some time to allow the system to
 * come back from a short-lived configuration change (such as rotations). If the view stops
 * observing for longer, the cache is kept but the upstream flows are stopped. When the view comes
 * back, the latest value is replayed and the upstream flows are executed again. This is done to
 * save resources when the app is in the background but let users switch between apps quickly.
 */
val WhileViewSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)

fun<T> Flow<Result<T>>.onSuccess(action: suspend (value: T) -> Unit) =
    onEach { it.onSuccess { action(it) } }

fun<T> Flow<Result<T>>.onFailure(action: suspend (exception: Throwable) -> Unit) =
    onEach {
        it.onFailure { error ->
            action(error)
        }
    }

fun<T, R> Flow<Result<T>>.mapResult(action: (T) -> R): Flow<Result<R>> =
    map { it.mapCatching(action) }

suspend fun<T> Flow<Result<T>>.execute(
    onStart: (() -> Unit)? = null,
    onSuccess: ((T) -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null,
) {
    this.onStart { onStart?.invoke() }
    collect {
        if (it.isSuccess) {
            it.getOrNull()?.let { value -> onSuccess?.invoke(value) }
        } else {
            it.exceptionOrNull()?.let { exception -> onError?.invoke(exception) }
        }
    }
}

suspend fun<T> Flow<Result<T>>.executeResult(onStart: (() -> Unit)? = null, onExecute: ((Result<T>) -> Unit)? = null) {
    onStart?.invoke()
    collect {
        onExecute?.invoke(it)
    }
}
