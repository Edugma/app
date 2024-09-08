package com.edugma.features.app.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.Ref
import androidx.compose.ui.unit.dp
import com.edugma.core.api.model.AppState
import com.edugma.core.api.model.NodeState
import com.edugma.core.api.model.SnackbarCommand
import com.edugma.core.api.model.ThemeMode
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.organism.snackbar.EdSnackbar
import com.edugma.core.designSystem.organism.snackbar.EdSnackbarStyle
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.elevation.EdElevation
import com.edugma.core.designSystem.utils.LocalEdIconLoader
import com.edugma.core.designSystem.utils.LocalEdImageLoader
import com.edugma.core.navigation.MainDestination
import com.edugma.core.navigation.core.rememberRouterNavigator
import com.edugma.core.navigation.nodes.NodesScreens
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.app.core.appScreens
import com.edugma.features.app.presentation.main.widgets.BottomNav
import com.edugma.features.app.presentation.main.widgets.TabContent
import com.edugma.features.app.presentation.main.widgets.rememberTabNavigator
import com.edugma.features.app.presentation.main.widgets.showNavBar
import com.edugma.features.nodes.nodesScreens
import com.edugma.navigation.core.compose.EdugmaTabNavigation
import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.graph.composeScreen
import com.edugma.navigation.core.utils.getRoute
import kotlinx.coroutines.flow.combine
import kotlin.time.Duration.Companion.seconds

@Composable
fun MainScreen(
    viewModel: MainAppViewModel = getViewModel(),
) {
    val state by viewModel.stateFlow.collectAsState()
    val appState = remember {
        derivedStateOf {
            state.appState
        }
    }

    EdTheme(
        useDynamicColors = true,
        useDarkTheme = when (state.themeMode) {
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
            ThemeMode.System -> isSystemInDarkTheme()
        },
    ) {
        EdSurface(
            color = EdTheme.colorScheme.background,
            elevation = EdElevation.Level0,
        ) {
            MainContent(
                appState = appState,
            )
        }
    }
}

@Composable
fun MainContent(
    appState: State<AppState>,
    viewModel: MainViewModel = getViewModel(),
) {
    val (tabNavigator, isNavigationBarVisible) = rememberTabNavigator(viewModel)

    var needTabMenuForHome by remember { mutableStateOf(false) }
    var needTabMenuForSchedule by remember { mutableStateOf(false) }
    var needTabMenuForAccount by remember { mutableStateOf(false) }
    var needTabMenuForMisc by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        combine(
            tabNavigator.navController.currentBackStackEntryFlow,
            snapshotFlow { needTabMenuForHome },
            snapshotFlow { needTabMenuForSchedule },
            snapshotFlow { needTabMenuForAccount },
            snapshotFlow { needTabMenuForMisc },
        ) { tabScreen, needForHome, needForSchedule, needForAccount, needForMisc ->
            tabScreen.destination.route == MainDestination.Home.getRoute() && needForHome ||
                tabScreen.destination.route == MainDestination.Schedule
                    .getRoute() && needForSchedule ||
                tabScreen.destination.route == MainDestination.Account
                    .getRoute() && needForAccount ||
                tabScreen.destination.route == MainDestination.Misc.getRoute() && needForMisc
        }.collect {
            isNavigationBarVisible.value = it
        }
    }

    val state by viewModel.stateFlow.collectAsState()

    val onAction = viewModel.rememberOnAction()

    CompositionLocalProvider(
        LocalEdImageLoader provides viewModel.commonImageLoader,
        LocalEdIconLoader provides viewModel.iconImageLoader,
    ) {
        Scaffold(
            bottomBar = { BottomNav(tabNavigator, viewModel.tabMenuRouter, isNavigationBarVisible) },
            contentWindowInsets = WindowInsets(0.dp),
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                var firstScreen: Destination? by remember { mutableStateOf(null) }
                LaunchedEffect(Unit) {
                    snapshotFlow { appState.value }.collect {
                        if (firstScreen == null) {
                            when (it.nodeState) {
                                NodeState.Initialization -> {
                                }
                                NodeState.Ready -> {
                                    firstScreen = MainDestination.Schedule
                                }
                                NodeState.Selection -> {
                                    firstScreen = NodesScreens.Main
                                }
                            }
                        }
                    }
                }

                firstScreen?.let {
                    EdugmaTabNavigation(tabNavigator, firstScreen!!) {
                        nodesScreens()
//                    composeScreen(MainDestination.Home) {
//                        val homeNavigator = rememberRouterNavigator(viewModel.homeRouter) {
//                            viewModel.tabMenuRouter.back()
//                        }
//                        LaunchedEffect(homeNavigator) {
//                            homeNavigator.navHostController.currentBackStackEntryFlow.collect {
//                                needTabMenuForHome = it.destination.route in showNavBar
//                            }
//                        }
//                        TabContent(homeNavigator, MainDestination.Home) {
//                            appScreens()
//                        }
//                    }
                        composeScreen(MainDestination.Schedule) {
                            val scheduleNavigator = rememberRouterNavigator(viewModel.scheduleRouter) {
                                viewModel.tabMenuRouter.back()
                            }
                            LaunchedEffect(scheduleNavigator) {
                                scheduleNavigator.navController.currentBackStackEntryFlow.collect {
                                    needTabMenuForSchedule = it.destination.route in showNavBar
                                }
                            }
                            TabContent(scheduleNavigator, MainDestination.Schedule) {
                                appScreens()
                            }
                        }
                        composeScreen(MainDestination.Account) {
                            val accountNavigator = rememberRouterNavigator(viewModel.accountRouter) {
                                viewModel.tabMenuRouter.back()
                            }
                            LaunchedEffect(accountNavigator) {
                                accountNavigator.navController.currentBackStackEntryFlow.collect {
                                    needTabMenuForAccount = it.destination.route in showNavBar
                                }
                            }
                            TabContent(accountNavigator, MainDestination.Account) {
                                appScreens()
                            }
                        }
                        composeScreen(MainDestination.Misc) {
                            val miscNavigator = rememberRouterNavigator(viewModel.miscRouter) {
                                viewModel.tabMenuRouter.back()
                            }
                            LaunchedEffect(miscNavigator) {
                                miscNavigator.navController.currentBackStackEntryFlow.collect {
                                    needTabMenuForMisc = it.destination.route in showNavBar
                                }
                            }
                            TabContent(miscNavigator, MainDestination.Misc) {
                                appScreens()
                            }
                        }
                    }
                }

                Snackbar(
                    messageProvider = { state.snackbars.firstOrNull() },
                    onDismissed = {
                        onAction(MainAction.OnSnackbarDismissed(it))
                    },
                    onAction = {
                        onAction(MainAction.OnSnackbarActionClicked(it))
                    },
                )
            }
        }
    }
}

@Composable
private fun BoxScope.Snackbar(
    messageProvider: () -> SnackbarCommand.Message?,
    onDismissed: (SnackbarCommand.Message) -> Unit,
    onAction: (SnackbarCommand.Message) -> Unit,
) {
    val message = messageProvider()

    val messageRef = remember {
        Ref<SnackbarCommand.Message>()
    }

    if (message != null) {
        messageRef.value = message
    }

    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
    ) {
        val message = messageRef.value!!
        val title = message.title.ifEmpty {
            when (message.type) {
                SnackbarCommand.Message.Type.Info -> "Важная информация"
                SnackbarCommand.Message.Type.Warning -> "Предупреждение"
                SnackbarCommand.Message.Type.Error -> "Ошибка"
            }
        }

        val style = when (message.type) {
            SnackbarCommand.Message.Type.Info -> EdSnackbarStyle.default
            SnackbarCommand.Message.Type.Warning -> EdSnackbarStyle.warning
            SnackbarCommand.Message.Type.Error -> EdSnackbarStyle.error
        }

        EdSnackbar(
            title = title,
            subtitle = message.subtitle,
            action = message.action.takeIf { message.needResult },
            // TODO
            // timeToDismiss = message.timeToDismiss ?: 6.seconds,
            timeToDismiss = 6.seconds,
            onDismissed = {
                onDismissed(message)
            },
            onActionClick = if (message.needResult) {
                { onAction(message) }
            } else {
                null
            },
            style = style,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 32.dp),
        )
    }
}
