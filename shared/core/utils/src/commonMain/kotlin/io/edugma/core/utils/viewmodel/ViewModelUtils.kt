package io.edugma.core.utils.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.node.Ref
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import io.edugma.core.api.utils.IO
import io.edugma.core.arch.mvi.stateStore.StateStoreBuilder
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.compose.koinInject

@Composable
inline fun <TState, TAction, reified T : BaseActionViewModel<TState, TAction>> getViewModel(): T {
    val viewModelStoreOwner = requireNotNull(LocalViewModelStoreOwner.current)

    val viewModelRef = remember(T::class) { Ref<T>() }

    if (viewModelRef.value == null) {
        val viewModelKey = T::class.qualifiedName.orEmpty()
        val viewModel = viewModelStoreOwner.viewModelStore.get(viewModelKey)
            ?: koinInject<T>().apply {
                viewModelStoreOwner.viewModelStore.put(viewModelKey, this)
            }
        viewModelRef.value = viewModel
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
        onError = onError,
        block = block,
    )
}
