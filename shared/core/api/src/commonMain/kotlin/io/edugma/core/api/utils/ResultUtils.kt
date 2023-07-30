package io.edugma.core.api.utils

import kotlinx.coroutines.CancellationException

public inline fun <R> runCoCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
