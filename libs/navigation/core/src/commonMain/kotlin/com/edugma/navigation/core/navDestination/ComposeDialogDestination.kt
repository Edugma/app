package com.edugma.navigation.core.navDestination

import androidx.compose.runtime.Composable
import com.edugma.navigation.core.destination.Destination

class ComposeDialogDestination(
    val destination: Destination,
    val composeScreen: @Composable () -> Unit,
) : NavDestination
