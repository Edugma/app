package io.edugma.navigation.core.navigator

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.edugma.navigation.core.screen.ScreenBundle

data class ScreenUiState(
    val screenBundle: ScreenBundle,
    val ui: @Composable (ScreenBundle) -> Unit,
    val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(),
    val instanceKeeperDispatcher: InstanceKeeperDispatcher = InstanceKeeperDispatcher(),
) : LifecycleOwner, InstanceKeeperOwner {
    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val instanceKeeper: InstanceKeeper
        get() = instanceKeeperDispatcher
}
