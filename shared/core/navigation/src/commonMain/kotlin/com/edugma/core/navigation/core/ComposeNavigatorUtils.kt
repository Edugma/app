package com.edugma.core.navigation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.edugma.core.api.api.CrashAnalytics
import com.edugma.core.api.utils.IO
import com.edugma.navigation.core.navigator.ComposeNavigator
import com.edugma.navigation.core.router.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun rememberRouterNavigator(
    router: Router,
    onCantBack: () -> Unit = {},
): ComposeNavigator {
    val navHostController = rememberNavController()
    val navigator = remember(navHostController, router) {
        ComposeNavigator(navHostController, router, onCantBack)
    }
    LaunchedEffect(navHostController) {
        launch(Dispatchers.IO) {
            navHostController.currentBackStackEntryFlow.collect {
                CrashAnalytics.setKey(CrashAnalytics.Key.CurrentScreen(it.destination.route.toString()))
            }
        }
    }
    LaunchedEffect(navigator) {
        navigator.listenCommands()
    }

    return navigator
}
