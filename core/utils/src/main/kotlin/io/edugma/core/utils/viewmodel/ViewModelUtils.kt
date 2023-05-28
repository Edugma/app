package io.edugma.core.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

@PublishedApi
internal fun ViewModel.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    errorHandler: CoroutineExceptionHandler? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    val coroutineContext = if (errorHandler != null) {
        dispatcher + errorHandler
    } else {
        dispatcher + ErrorHandler {
            Logger.e("launchCoroutine: ", it, tag = "ViewModelCoroutine")
        }
    }
    viewModelScope.launch(
        context = coroutineContext,
        block = block,
    )
}
inline fun ViewModel.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline onError: (Throwable) -> Unit? = {},
    noinline block: suspend CoroutineScope.() -> Unit,
) {
    launchCoroutine(
        dispatcher = dispatcher,
        errorHandler = ErrorHandler {
            onError(it)
        },
        block = block,
    )
}

@Suppress("FunctionName")
inline fun ErrorHandler(crossinline handler: (Throwable) -> Unit): CoroutineExceptionHandler =
    object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
        override fun handleException(context: CoroutineContext, exception: Throwable) =
            handler.invoke(exception)
    }
