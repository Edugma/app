package io.edugma.navigation.core.navigator

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import io.edugma.navigation.core.screen.ScreenBundle

data class ScreenUiState(
    val screenBundle: ScreenBundle,
    val ui: @Composable (ScreenBundle) -> Unit,
    private var _lifecycleRegistry = LifecycleRegistry(this),
    override val viewModelStore: ViewModelStore = ViewModelStore(),
) : LifecycleOwner, ViewModelStoreOwner {

    override val lifecycle: Lifecycle
        get() = _lifecycleRegistry

    fun lifecycleCreate() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun lifecycleDestroy() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun lifecycleReset() {
        _lifecycleRegistry = LifecycleRegistry(this)
    }
}
