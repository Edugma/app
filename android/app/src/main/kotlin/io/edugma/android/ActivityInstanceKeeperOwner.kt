package io.edugma.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import io.edugma.navigation.core.instanceKeeper.LocalInstanceKeeperOwner

@Composable
fun ProvideActivityInstanceKeeperOwner(content: @Composable () -> Unit) {
    val activityInstanceKeeperOwner = viewModel<ActivityInstanceKeeperOwner>()

    CompositionLocalProvider(
        LocalInstanceKeeperOwner provides activityInstanceKeeperOwner,
        content = content,
    )
}

class ActivityInstanceKeeperOwner : ViewModel(), InstanceKeeperOwner {
    private val instanceKeeperDispatcher = InstanceKeeperDispatcher()

    override val instanceKeeper: InstanceKeeper
        get() = instanceKeeperDispatcher

    override fun onCleared() {
        super.onCleared()

        instanceKeeperDispatcher.destroy()
    }
}
