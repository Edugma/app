package com.edugma.core.navigation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.edugma.navigation.core.navigator.ComposeNavigator
import com.edugma.navigation.core.router.Router

@Composable
fun rememberRouterNavigator(
    router: Router,
    onCantBack: () -> Unit = {},
): ComposeNavigator {
    val navHostController = rememberNavController()
    val navigator = remember(navHostController, router) {
        ComposeNavigator(navHostController, router, onCantBack)
    }
    LaunchedEffect(navigator) {
        navigator.listenCommands()
    }

    return navigator
}
