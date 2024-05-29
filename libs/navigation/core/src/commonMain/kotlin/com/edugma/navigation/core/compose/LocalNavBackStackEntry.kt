package com.edugma.navigation.core.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavBackStackEntry

val LocalNavBackStackEntry: ProvidableCompositionLocal<NavBackStackEntry> = staticCompositionLocalOf {
    error("NavBackStackEntry not present")
}
