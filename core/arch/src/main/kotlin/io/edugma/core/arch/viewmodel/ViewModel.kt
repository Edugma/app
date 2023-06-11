package io.edugma.core.arch.viewmodel

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.compose.koinInject

open class ViewModel {

    val viewModelScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun clear() {
        viewModelScope.cancel()
    }

    open fun onCleared() {
    }
}

@Composable
inline fun <reified T : ViewModel> getViewModel(): T {
    return koinInject<T>()
}
