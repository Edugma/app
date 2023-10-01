package io.edugma.core.utils.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.node.Ref
import co.touchlab.kermit.Logger
import com.arkivanov.essenty.instancekeeper.getOrCreate
import io.edugma.core.arch.mvi.stateStore.StateStoreBuilder
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.arch.viewmodel.RestrictedApi
import io.edugma.navigation.core.instanceKeeper.LocalInstanceKeeperOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

@Composable
inline fun <TState, TAction, reified T : BaseActionViewModel<TState, TAction>> getViewModel(): T {
    val instanceKeeperOwner = requireNotNull(LocalInstanceKeeperOwner.current)

    val viewModelRef = remember(T::class) { Ref<T>() }

    if (viewModelRef.value == null) {
        viewModelRef.value = instanceKeeperOwner.instanceKeeper.getOrCreate {
            koinInject<T>()
        }
    }
    val viewModel = viewModelRef.value!!

    val defaultErrorHandler = koinInject<DefaultErrorHandler>()

    StateStoreBuilder(
        stateStore = viewModel,
    ).apply {
        errorHandler(defaultErrorHandler)
    }.build()

    return viewModel
}

inline fun BaseActionViewModel<*, *>.launchCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline onError: (Throwable) -> Unit = {},
    noinline block: suspend CoroutineScope.() -> Unit,
): Job {
    return launchCoroutine(
        dispatcher = dispatcher,
        errorHandler = ErrorHandler { cont, it ->
            this.errorHandler?.handleException(cont, it)
            onError(it)
            Logger.e("ViewModel launchCoroutine error: ", it, tag = "launchCoroutine")
        },
        block = block,
    )
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
