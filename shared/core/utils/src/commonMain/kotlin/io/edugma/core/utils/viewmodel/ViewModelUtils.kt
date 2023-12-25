package io.edugma.core.utils.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.node.Ref
import com.arkivanov.essenty.instancekeeper.getOrCreate
import io.edugma.core.api.utils.IO
import io.edugma.core.arch.mvi.stateStore.StateStoreBuilder
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.navigation.core.instanceKeeper.LocalInstanceKeeperOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.compose.koinInject

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
        onError = onError,
        block = block,
    )
}
