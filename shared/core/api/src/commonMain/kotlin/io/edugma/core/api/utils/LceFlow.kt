package io.edugma.core.api.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
fun <T> lce(@BuilderInference block: suspend LceFlowCollector<T>.() -> Unit): LceFlow<T> {
    return LceFlow<T>(
        flow {
            val collector = object : LceFlowCollector<T> {
                override suspend fun emitSuccess(value: T, isLoading: Boolean) {
                    emit(LceData(Result.success(value), isLoading))
                }

                override suspend fun emitResult(result: Result<T>, isLoading: Boolean) {
                    emit(LceData(result, isLoading))
                }

                override suspend fun emitFailure(exception: Throwable, isLoading: Boolean) {
                    emit(LceData(Result.failure(exception), isLoading))
                }
            }
            block(collector)
        },
    )
}

public interface LceFlowCollector<in T> {
    /**
     * Collects the value emitted by the upstream.
     * This method is not thread-safe and should not be invoked concurrently.
     */
    public suspend fun emitResult(result: Result<T>, isLoading: Boolean)

    /**
     * Collects the value emitted by the upstream.
     * This method is not thread-safe and should not be invoked concurrently.
     */
    public suspend fun emitSuccess(value: T, isLoading: Boolean)

    /**
     * Collects the value emitted by the upstream.
     * This method is not thread-safe and should not be invoked concurrently.
     */
    public suspend fun emitFailure(exception: Throwable, isLoading: Boolean)
}

class LceFlow<out T>(
    @PublishedApi
    internal val flow: Flow<LceData<T>>,
) {
    companion object {
        fun <T> content(value: T): LceFlow<T> {
            return LceFlow(flowOf(LceData(Result.success(value), false)))
        }

        fun empty(): LceFlow<Nothing> {
            return LceFlow(flowOf(LceData(Result.failure(Exception()), false)))
        }
    }
}

suspend inline fun <T> LceFlow<T>.onResult(
    crossinline onSuccess: (LceSuccess<T>) -> Unit,
    crossinline onFailure: (LceFailure) -> Unit,
) {
    this.flow.collect { lceData ->
        lceData.result.onSuccess {
            onSuccess(lceData)
        }.onFailure {
            onFailure(lceData)
        }
    }
}

class LceData<out T>(
    @PublishedApi
    internal val result: Result<T>,
    /**
     * Returns `true` if this instance represents a loading outcome.
     */
    override val isLoading: Boolean,
) : LceSuccess<T>, LceFailure {
    override val value: T
        get() = result.getOrThrow()

    override val exception: Throwable
        get() = result.exceptionOrNull()!!
}

interface LceSuccess<out T> {
    val value: T
    val isLoading: Boolean
}

interface LceFailure {
    val exception: Throwable
    val isLoading: Boolean
}

inline fun <T, R> LceFlow<T>.map(crossinline transform: (T) -> R): LceFlow<R> {
    return LceFlow<R>(
        flow.map { oldLceData ->
            LceData(
                result = oldLceData.result.map { transform(it) },
                isLoading = oldLceData.isLoading,
            )
        },
    )
}
