package io.edugma.navigation.core.navDestination

import androidx.compose.runtime.Composable
import io.edugma.navigation.core.screen.Destination

class ComposeDialogDestination(
    val destination: Destination,
    val composeScreen: @Composable () -> Unit,
) : NavDestination
