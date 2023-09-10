package io.edugma.navigation.core.lifecycle

import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.essenty.lifecycle.LifecycleOwner

val LocalScreenLifecycleOwner = staticCompositionLocalOf<LifecycleOwner?> { null }
