package io.edugma.navigation.core.instanceKeeper

import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner

val LocalInstanceKeeperOwner = staticCompositionLocalOf<InstanceKeeperOwner?> { null }
