package io.edugma.core.arch.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

open class ViewModel : InstanceKeeper.Instance {

    val viewModelScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    open fun onCleared() {
    }

    override fun onDestroy() {
        viewModelScope.cancel()
        onCleared()
    }
}

@Composable
inline fun <reified T : ViewModel> T.bind(crossinline onBind: () -> Unit) {
    DisposableEffect(key1 = this) {
        onBind()
        onDispose {
        }
    }
}
