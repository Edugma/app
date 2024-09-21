package com.edugma.core.arch.mvi.utils

import com.edugma.core.api.api.CrashAnalytics
import com.edugma.core.api.utils.IO
import com.edugma.core.arch.mvi.delegate.DebounceDelegate
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

inline fun FeatureLogic<*, *>.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline onError: (Throwable) -> Unit = {},
    noinline block: suspend CoroutineScope.() -> Unit,
): Job {
    return launchCoroutine(
        dispatcher = dispatcher,
        errorHandler = ErrorHandler { cont, it ->
            errorHandler?.handleException(cont, it)
            onError(it)
            CrashAnalytics.logException("launchCoroutine", "ViewModel launchCoroutine error: ", it)
        },
        block = block,
    )
}

@PublishedApi
internal fun FeatureLogic<*, *>.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    errorHandler: CoroutineExceptionHandler? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    val coroutineContext = if (errorHandler != null) {
        dispatcher + errorHandler
    } else {
        dispatcher + ErrorHandler { _, it ->
            CrashAnalytics.logException("launchCoroutine", "launchCoroutine: ", it)
        }
    }
    return scope.launch(
        context = coroutineContext,
        block = block,
    )
}

inline fun DebounceDelegate.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline onError: (Throwable) -> Unit = {},
    noinline block: suspend CoroutineScope.() -> Unit,
): Job {
    return launchCoroutine(
        dispatcher = dispatcher,
        errorHandler = ErrorHandler { cont, it ->
            errorHandler?.handleException(cont, it)
            onError(it)
            CrashAnalytics.logException("launchCoroutine", "ViewModel launchCoroutine error: ", it)
        },
        block = block,
    )
}

@PublishedApi
internal fun DebounceDelegate.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    errorHandler: CoroutineExceptionHandler? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    val coroutineContext = if (errorHandler != null) {
        dispatcher + errorHandler
    } else {
        dispatcher + ErrorHandler { _, it ->
            CrashAnalytics.logException("launchCoroutine", "launchCoroutine: ", it)
        }
    }
    return with(scope) {
        launchDebounce(
            context = coroutineContext,
            block = block,
        )
    }
}

@Suppress("FunctionName")
inline fun ErrorHandler(crossinline handler: (CoroutineContext, Throwable) -> Unit): CoroutineExceptionHandler =
    object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
        override fun handleException(context: CoroutineContext, exception: Throwable) =
            handler.invoke(context, exception)
    }
