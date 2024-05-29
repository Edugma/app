package com.edugma.core.arch.mvi.utils

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.edugma.core.api.utils.IO
import com.edugma.core.arch.mvi.delegate.DebounceDelegate
import com.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import com.edugma.core.arch.mvi.viewmodel.ViewModelDelegate
import com.edugma.core.arch.viewmodel.RestrictedApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

inline fun BaseActionViewModel<*, *>.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline onError: (Throwable) -> Unit = {},
    noinline block: suspend CoroutineScope.() -> Unit,
): Job {
    return launchCoroutine(
        dispatcher = dispatcher,
        errorHandler = ErrorHandler { cont, it ->
            errorHandler?.handleException(cont, it)
            onError(it)
            Logger.e("ViewModel launchCoroutine error: ", it, tag = "launchCoroutine")
        },
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
            Logger.e("ViewModel launchCoroutine error: ", it, tag = "launchCoroutine")
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
            Logger.e("launchCoroutine: ", it, tag = "ViewModelCoroutine")
        }
    }
    return with(scope) {
        launchDebounce(
            context = coroutineContext,
            block = block,
        )
    }
}

@PublishedApi
internal fun BaseActionViewModel<*, *>.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    errorHandler: CoroutineExceptionHandler? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    val coroutineContext = if (errorHandler != null) {
        dispatcher + errorHandler
    } else {
        dispatcher + ErrorHandler { _, it ->
            Logger.e("launchCoroutine: ", it, tag = "ViewModelCoroutine")
        }
    }
    return viewModelScope.launch(
        context = coroutineContext,
        block = block,
    )
}

inline fun ViewModelDelegate<*>.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline onError: (Throwable) -> Unit = {},
    noinline block: suspend CoroutineScope.() -> Unit,
): Job {
    return launchCoroutineInternal(
        dispatcher = dispatcher,
        errorHandler = ErrorHandler { cont, it ->
            errorHandler?.handleException(cont, it)
            onError(it)
            Logger.e("ViewModel launchCoroutine error: ", it, tag = "launchCoroutine")
        },
        block = block,
    )
}

@PublishedApi
internal fun ViewModelDelegate<*>.launchCoroutineInternal(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    errorHandler: CoroutineExceptionHandler? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    val coroutineContext = if (errorHandler != null) {
        dispatcher + errorHandler
    } else {
        dispatcher + ErrorHandler { _, it ->
            Logger.e("launchCoroutine: ", it, tag = "ViewModelCoroutine")
        }
    }
    @OptIn(RestrictedApi::class)
    return viewModelScope.launch(
        context = coroutineContext,
        block = block,
    )
}

@Suppress("FunctionName")
inline fun ErrorHandler(crossinline handler: (CoroutineContext, Throwable) -> Unit): CoroutineExceptionHandler =
    object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
        override fun handleException(context: CoroutineContext, exception: Throwable) =
            handler.invoke(context, exception)
    }
